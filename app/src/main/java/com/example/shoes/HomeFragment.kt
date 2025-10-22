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
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        // 播放首页视频（优先本地 raw 资源，失败时自动切换到远程 MP4）
        val player = ExoPlayer.Builder(requireContext()).build()
        binding.homeVideo.player = player

        val localVideoUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.shoes)
        val localItem = MediaItem.fromUri(localVideoUri)

        fun play(mediaItem: MediaItem) {
            player.setMediaItem(mediaItem)
            player.repeatMode = Player.REPEAT_MODE_ONE
            player.prepare()
            player.playWhenReady = true
        }

        // 先尝试播放本地
        play(localItem)

        // 缓冲完成后淡出海报；错误时保留海报并提示
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    binding.banner.animate().alpha(0f).setDuration(200).start()
                }
            }
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                // 如果是容器不支持，尝试切换到远程 MP4
                if (error.errorCodeName == "ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED") {
                    try {
                        val remoteItem = MediaItem.fromUri(RemoteConfig.homeVideoUrl)
                        play(remoteItem)
                        Toast.makeText(requireContext(), "切换为在线播放…", Toast.LENGTH_SHORT).show()
                        return
                    } catch (_: Throwable) { /* ignore */ }
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
        return binding.root
    }

    private fun setupList() {
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        lifecycleScope.launchWhenStarted {
            try {
                val remote = com.example.shoes.data.RemoteRepository().products()
                binding.list.adapter = ProductAdapter(remote) { product ->
                    startActivity(ProductDetailActivity.intent(requireContext(), product.id))
                }
            } catch (e: Exception) {
                // 回退到本地假数据
                val local = ProductRepository.list()
                binding.list.adapter = ProductAdapter(local) { product ->
                    startActivity(ProductDetailActivity.intent(requireContext(), product.id))
                }
                Toast.makeText(requireContext(), "使用本地数据（原因：${e.message})", Toast.LENGTH_SHORT).show()
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
}
