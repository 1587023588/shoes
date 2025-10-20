package com.example.shoes

import android.Manifest
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

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var initialized = false
    private val TAG = "ScanActivity"

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startScanning() else Toast.makeText(this, "需要相机权限才能扫码", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // 1) 设备是否具备相机（模拟器常见为不具备）
            val hasCameraAny = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            if (!hasCameraAny) {
                Toast.makeText(this, "当前设备不支持相机（可能是模拟器），请使用真机扫码。", Toast.LENGTH_LONG).show()
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

            binding.toolbar.setNavigationOnClickListener { finish() }
            // 若设备无闪光灯则隐藏手电按钮
            val hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            binding.btnTorch.visibility = if (hasFlash) View.VISIBLE else View.GONE
            binding.btnTorch.setOnClickListener { toggleTorch() }

            // 动态创建 DecoratedBarcodeView，避免某些环境 XML InflateException
            val barcodeView = DecoratedBarcodeView(this)
            barcodeView.layoutParams = android.widget.FrameLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
            binding.barcodeContainer.addView(barcodeView)

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
        } catch (t: Throwable) {
            // 当依赖还未正确打包到 APK 或其它初始化异常时，避免直接崩溃
            Log.e(TAG, "Scan init failed: ${t.javaClass.simpleName}: ${t.message}", t)
            Toast.makeText(this, "扫一扫组件未就绪（${t.javaClass.simpleName}），请重新安装最新构建。", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (initialized) ensureCameraPermissionThenStart()
    }

    override fun onPause() {
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
        findBarcodeView()?.resume()
        binding.resultCard.visibility = View.GONE
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
}
