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
        // 播放首页视频（COS）
        binding.homeVideo.setVideoURI(Uri.parse(RemoteConfig.homeVideoUrl))
        binding.homeVideo.setOnPreparedListener { mp ->
            mp.isLooping = true
            // 准备好后隐藏海报
            binding.banner.animate().alpha(0f).setDuration(200).start()
            binding.homeVideo.start()
        }
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
        // 释放视频
        try {
            binding.homeVideo.stopPlayback()
        } catch (_: Throwable) {}
        _binding = null
    }
}
