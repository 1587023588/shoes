package com.example.shoes

import android.annotation.SuppressLint
import android.os.Bundle
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.lifecycle.lifecycleScope
import com.example.shoes.data.ProductRepository
import coil.load
import com.example.shoes.databinding.FragmentHomeBinding
import com.example.shoes.ui.home.ProductAdapter
import com.example.shoes.net.Session
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var triedRemoteFallback = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // 视频海报先显示
        binding.banner.load(RemoteConfig.homeVideoPoster) {
            crossfade(true)
            placeholder(R.drawable.tab_mine)
            error(R.drawable.tab_mine)
        }
    // 播放首页视频（先尝试本地 raw 资源），使用 Media3 ExoPlayer 适配更多模拟器
        val player = ExoPlayer.Builder(requireContext()).build()
        binding.homeVideo.player = player
    val localVideoUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.shoes)
    val item = MediaItem.fromUri(localVideoUri)
        player.setMediaItem(item)
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.prepare()
        player.playWhenReady = true

        // 缓冲完成后淡出海报；错误时保留海报并提示
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    binding.banner.animate().alpha(0f).setDuration(200).start()
                }
            }
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                // 本地资源出错时回退到远程 URL 再试一次
                if (!triedRemoteFallback) {
                    triedRemoteFallback = true
                    try {
                        val remoteItem = MediaItem.fromUri(RemoteConfig.homeVideoUrl)
                        player.setMediaItem(remoteItem)
                        player.prepare()
                        player.playWhenReady = true
                        Toast.makeText(requireContext(), "本地视频失败，尝试在线播放…", Toast.LENGTH_SHORT).show()
                        return
                    } catch (_: Throwable) { /* ignore and保持提示 */ }
                }
                Toast.makeText(requireContext(), "视频播放失败：" + error.errorCodeName, Toast.LENGTH_SHORT).show()
                binding.banner.alpha = 1f
            }
        })
        setupList()

        // 顶部搜索与相机点击占位
        binding.btnSearch.setOnClickListener {
            val keyword = binding.etSearch.text?.toString()?.trim().orEmpty()
            Toast.makeText(requireContext(), "搜索：$keyword", Toast.LENGTH_SHORT).show()
        }
        binding.btnCamera.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), ScanActivity::class.java))
        }
        // 预约日历：直接跳转预约参观页（固定 09:00-17:00）
        binding.actionCalendar.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), BookingActivity::class.java))
        }
        // 红色场馆：跳转“云上场馆”页
        binding.actionMuseum.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), MuseumActivity::class.java))
        }
        // 首页功能区的“扫一扫”入口
        binding.actionScan.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), ScanActivity::class.java))
        }

        // 未登录提示：去登录
        binding.btnGoLogin.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), LoginActivity::class.java))
        }
        return binding.root
    }

    private fun setupList() {
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        // 放在 NestedScrollView 中，由外层负责滚动
        binding.list.isNestedScrollingEnabled = false
        // 优先从内存取；若为空则尝试从 SharedPreferences 恢复
        var token = Session.token
        if (token.isNullOrEmpty()) {
            try {
                val sp = requireContext().getSharedPreferences("session", android.content.Context.MODE_PRIVATE)
                val saved = sp.getString("auth_token", null)
                if (!saved.isNullOrEmpty()) {
                    Session.token = saved
                    token = saved
                }
            } catch (_: Throwable) {}
        }
        if (token.isNullOrEmpty()) {
            binding.loginHintContainer.visibility = View.VISIBLE
            // 未登录：仅展示示例 A/B（本地前两条）
            val samples = ProductRepository.list().take(2)
            binding.list.adapter = ProductAdapter(samples) { product ->
                // 可允许查看详情，也可在此处引导登录；先保留查看详情
                startActivity(ProductDetailActivity.intent(requireContext(), product.id))
            }
            // 轻提示
            Toast.makeText(requireContext(), "登录后可查看全部商品", Toast.LENGTH_SHORT).show()
        } else {
            binding.loginHintContainer.visibility = View.GONE
            // 已登录：与“线上销售”页保持一致，直接使用本地完整商品库（带完善图片与介绍）
            val all = ProductRepository.list()
            binding.list.adapter = ProductAdapter(all) { product ->
                startActivity(ProductDetailActivity.intent(requireContext(), product.id))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 释放播放器
        try {
            val player = binding.homeVideo.player as? ExoPlayer
            player?.release()
            binding.homeVideo.player = null
        } catch (_: Throwable) {}
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // 登录状态可能在其他页面发生变化，这里刷新商品列表
        try { setupList() } catch (_: Throwable) {}
    }
}
