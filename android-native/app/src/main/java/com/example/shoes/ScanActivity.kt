package com.example.shoes

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoes.databinding.ActivityScanBinding
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import android.hardware.camera2.CameraManager
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var initialized = false
    private val TAG = "ScanActivity"
    private var isResumed = false
    private var externalLaunching = false
    private var pendingExternalOptions: ScanOptions? = null
    // 若你的设备/模拟器经常在内嵌预览崩溃，将此开关设为 true 仅使用外置扫码页
    private val USE_EXTERNAL_ONLY = true

    // ZXing 4.x 推荐的外部扫描启动器（替代 IntentIntegrator）
    private val externalScannerLauncher = registerForActivityResult(ScanContract()) { result ->
        result?.let {
            val contents = it.contents
            if (contents != null) {
                if (!initialized) {
                    Toast.makeText(this, "结果：$contents", Toast.LENGTH_LONG).show()
                } else {
                    binding.resultText.text = contents
                    binding.resultCard.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "已取消", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startScanning() else Toast.makeText(this, "需要相机权限才能扫码", Toast.LENGTH_SHORT).show()
        }

    // 从相册选择图片
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { handlePickedImage(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            breadcrumb("onCreate-start")
            // 1) 设备是否具备相机（模拟器常见为不具备）
            val hasCameraAny = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            if (!hasCameraAny) {
                Toast.makeText(this, "当前设备不支持相机（可能是模拟器），请使用真机扫码。", Toast.LENGTH_LONG).show()
                finish()
                return
            }

            // 进一步检测是否真的存在可用的 cameraId（部分模拟器错误报告支持相机）
            if (!hasUsableCamera()) {
                Toast.makeText(this, "未检测到可用的相机（多见于模拟器），请用真机扫码。", Toast.LENGTH_LONG).show()
                finish()
                return
            }

            // 2) 必要类是否已打包进 APK（若未打包会 ClassNotFound）
            runCatching { Class.forName("com.journeyapps.barcodescanner.DecoratedBarcodeView") }
                .onFailure {
                    Log.e(TAG, "ZXing DecoratedBarcodeView not found", it)
                    Toast.makeText(this, "扫一扫组件未就绪，请重新安装最新构建。", Toast.LENGTH_LONG).show()
                    finish()
                    return
                }

            binding = ActivityScanBinding.inflate(layoutInflater)
            setContentView(binding.root)
            breadcrumb("setContentView-done")

            binding.toolbar.setNavigationOnClickListener { finish() }
            // 添加菜单：日志/相册识别
            binding.toolbar.inflateMenu(R.menu.menu_scan)
            binding.toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_logs -> {
                        startActivity(Intent(this, LogsActivity::class.java))
                        true
                    }
                    R.id.menu_pick -> {
                        pickImageLauncher.launch("image/*")
                        true
                    }
                    else -> false
                }
            }
            // 若设备无闪光灯则隐藏手电按钮
            val hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            binding.btnTorch.visibility = if (hasFlash) View.VISIBLE else View.GONE
            binding.btnTorch.setOnClickListener { toggleTorch() }

            if (USE_EXTERNAL_ONLY) {
                // 纯外置扫码模式：不初始化内嵌相机，直接拉起内置扫码页
                breadcrumb("external-only-manual")
                initialized = false
                // 展示操作面板，避免直接拉起相机导致黑屏覆盖顶部菜单
                binding.actionsPanel.visibility = View.VISIBLE
                binding.btnStartExternal.setOnClickListener {
                    breadcrumb("external-only-click-start")
                    scheduleExternalScannerLaunch()
                }
                binding.btnPickFromGallery.setOnClickListener {
                    breadcrumb("external-only-click-pick")
                    pickImageLauncher.launch("image/*")
                }
            } else {
                // 动态创建 DecoratedBarcodeView，避免某些环境 XML InflateException
                val barcodeView = DecoratedBarcodeView(this)
                // 许多机型在 Camera2 下会抛 IllegalArgumentException，强制使用老的 Camera1 更稳定
                runCatching {
                    val settings = barcodeView.barcodeView.cameraSettings
                    // 优先通过反射调用 setUseCamera2(false)，以兼容不同版本的 zxing-android-embedded
                    runCatching {
                        settings.javaClass
                            .getMethod("setUseCamera2", Boolean::class.javaPrimitiveType)
                            .invoke(settings, false)
                    }
                    settings.setAutoFocusEnabled(true)
                }
                barcodeView.layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                binding.barcodeContainer.addView(barcodeView)

                // 提前只创建视图与回调，真正打开相机在 onResume -> startScanning 中执行，避免某些机型初始化时抛出异常
                barcodeView.decodeContinuous(object : BarcodeCallback {
                    override fun barcodeResult(result: BarcodeResult?) {
                        result?.text?.let { content ->
                            binding.resultText.text = content
                            binding.resultCard.visibility = View.VISIBLE
                        }
                    }
                    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
                })
                initialized = true
                breadcrumb("embedded-init-done")
            }
        } catch (t: Throwable) {
            // 当依赖还未正确打包到 APK 或其它初始化异常时，避免直接崩溃
            Log.e(TAG, "Scan init failed: ${t.javaClass.simpleName}: ${t.message}", t)
            breadcrumb("onCreate-exception-${t.javaClass.simpleName}")
            // 一律尝试使用 ZXing 内置 CaptureActivity 作为兜底（包括 IllegalArgumentException 等）
            // 注意：不能在 onCreate 尚未 RESUMED 时直接 launch，需延迟到 onResume
            Toast.makeText(this, "扫一扫组件出现异常（${t.javaClass.simpleName}），已切换内置扫码页。", Toast.LENGTH_LONG).show()
            scheduleExternalScannerLaunch()
            return
        }
    }

    override fun onResume() {
        super.onResume()
        isResumed = true
        breadcrumb("onResume")
        // 若有挂起的外部扫码任务，优先执行
        maybeLaunchPendingExternalScanner()
        if (initialized) ensureCameraPermissionThenStart()
    }

    override fun onPause() {
        isResumed = false
        breadcrumb("onPause")
        if (initialized) findBarcodeView()?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        if (initialized) {
            try { findBarcodeView()?.pause() } catch (_: Throwable) {}
        }
        super.onDestroy()
    }

    private fun ensureCameraPermissionThenStart() {
        val has = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (has) startScanning() else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startScanning() {
        try {
            breadcrumb("startScanning")
            findBarcodeView()?.resume()
            binding.resultCard.visibility = View.GONE
        } catch (e: IllegalArgumentException) {
            // 某些设备在打开相机时会抛 IllegalArgumentException，此时自动转到兜底扫描页
            Log.e(TAG, "Start scanning failed: ${e.message}", e)
            breadcrumb("startScanning-IAE")
            Toast.makeText(this, "相机初始化异常，已切换内置扫码页。", Toast.LENGTH_SHORT).show()
            cleanupAndFallback()
        } catch (e: IllegalStateException) {
            // LifecycleOwner 未处于 RESUMED 状态时发起，会抛此异常；改为延迟到 onResume
            Log.e(TAG, "Start scanning state error: ${e.message}", e)
            breadcrumb("startScanning-ISE")
            cleanupAndFallback()
        } catch (e: Throwable) {
            Log.e(TAG, "Start scanning unexpected error: ${e.message}", e)
            breadcrumb("startScanning-OTHER-${e.javaClass.simpleName}")
            Toast.makeText(this, "相机初始化失败（${e.javaClass.simpleName}）", Toast.LENGTH_SHORT).show()
        }
    }

    private var torchOn = false
    private fun toggleTorch() {
        torchOn = !torchOn
        if (torchOn) {
            findBarcodeView()?.setTorchOn()
            binding.btnTorch.text = "关灯"
        } else {
            findBarcodeView()?.setTorchOff()
            binding.btnTorch.text = "开灯"
        }
    }

    private fun findBarcodeView(): DecoratedBarcodeView? {
        val child = binding.barcodeContainer.getChildAt(0)
        return child as? DecoratedBarcodeView
    }

    // 使用 ZXing 4.x 的 ScanContract 作为兜底
    private fun launchExternalScanner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setPrompt("对准二维码/条码")
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
        }
        externalScannerLauncher.launch(options)
    }

    private fun scheduleExternalScannerLaunch() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setPrompt("对准二维码/条码")
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
        }
        // 若当前尚未处于 RESUMED，则挂起到 onResume 再执行
        if (!isResumed) {
            breadcrumb("scheduleExternalScannerLaunch-pending")
            pendingExternalOptions = options
        } else {
            runCatching {
                externalLaunching = true
                breadcrumb("scheduleExternalScannerLaunch-launch")
                externalScannerLauncher.launch(options)
            }.onFailure {
                Log.e(TAG, "Launch external scanner failed", it)
                breadcrumb("scheduleExternalScannerLaunch-fail-${it.javaClass.simpleName}")
                Toast.makeText(this, "内置扫码页启动失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun maybeLaunchPendingExternalScanner() {
        val options = pendingExternalOptions ?: return
        if (externalLaunching) return
        breadcrumb("maybeLaunchPendingExternalScanner")
        pendingExternalOptions = null
        runCatching {
            externalLaunching = true
            externalScannerLauncher.launch(options)
        }.onFailure {
            Log.e(TAG, "Pending external scanner launch failed", it)
            breadcrumb("pendingExternalScanner-fail-${it.javaClass.simpleName}")
            Toast.makeText(this, "内置扫码页启动失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cleanupAndFallback() {
        // 先停止并移除内嵌相机预览，避免相机后台线程继续运行引发崩溃
        runCatching { findBarcodeView()?.pause() }
        runCatching { binding.barcodeContainer.removeAllViews() }
        initialized = false
        breadcrumb("cleanupAndFallback")
        scheduleExternalScannerLaunch()
    }

    private fun hasUsableCamera(): Boolean {
        return try {
            val cm = getSystemService(CameraManager::class.java)
            val ids = cm?.cameraIdList ?: emptyArray()
            ids.isNotEmpty()
        } catch (e: Throwable) {
            Log.e(TAG, "Check camera error: ${e.message}", e)
            false
        }
    }

    private fun breadcrumb(msg: String) {
        runCatching {
            val dir = java.io.File(filesDir, "logs"); if (!dir.exists()) dir.mkdirs()
            val f = java.io.File(dir, "scan_breadcrumbs.txt")
            f.appendText("${System.currentTimeMillis()} ${msg}\n")
        }
    }

    private fun handlePickedImage(uri: Uri) {
        runCatching {
            val bitmap = loadBitmapFromUri(uri)
            val resultText = decodeBitmapWithZXing(bitmap)
            if (resultText != null) {
                binding.resultText.text = resultText
                binding.resultCard.visibility = View.VISIBLE
                Toast.makeText(this, "识别成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "未识别到二维码/条码", Toast.LENGTH_SHORT).show()
            }
        }.onFailure {
            Log.e(TAG, "Pick image decode failed", it)
            Toast.makeText(this, "图片识别失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            android.provider.MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    private fun decodeBitmapWithZXing(bitmap: Bitmap): String? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        val binBitmap = BinaryBitmap(HybridBinarizer(source))
        return try {
            val result = MultiFormatReader().decode(binBitmap)
            result.text
        } catch (_: NotFoundException) {
            null
        } catch (e: Throwable) {
            Log.e(TAG, "ZXing decode error", e)
            null
        }
    }
}
