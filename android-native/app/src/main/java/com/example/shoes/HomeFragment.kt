package com.example.shoes

import android.os.Bundle
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
    // 播放首页视频（本地 raw 资源），使用 Media3 ExoPlayer 适配更多模拟器
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
            Toast.makeText(requireContext(), "打开相机", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun setupList() {
        val products = ProductRepository.list()
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.list.adapter = ProductAdapter(products) { product ->
            startActivity(ProductDetailActivity.intent(requireContext(), product.id))
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
