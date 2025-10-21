package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.shoes.databinding.ActivityBoutiqueShoesBinding
 

class BoutiqueShoesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoutiqueShoesBinding
    private val handler = Handler(Looper.getMainLooper())
    private var autoScroll = true
    private var bannerUrls: List<String> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoutiqueShoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 全屏黑背景轮播：对齐“云上场馆”样式
        val base = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/shoes/"
        bannerUrls = (1..31).map { i -> "%02d.jpg".format(i) }.map { base + it }
        binding.bannerViewPager.adapter = BannerAdapter(bannerUrls)
        // 图片适配：使用 FIT_CENTER，避免被裁剪（通过 Item 布局上的 scaleType 或在 BannerAdapter 中设置）

        // 指示器
        setupIndicators()
        binding.bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        // 左右按钮
        binding.btnPrev.setOnClickListener {
            val size = bannerUrls.size
            if (size > 0) binding.bannerViewPager.currentItem = (binding.bannerViewPager.currentItem - 1 + size) % size
        }
        binding.btnNext.setOnClickListener {
            val size = bannerUrls.size
            if (size > 0) binding.bannerViewPager.currentItem = (binding.bannerViewPager.currentItem + 1) % size
        }

    // 无下方宫格（已改为全屏轮播样式）

        // 自动滚动
        startAutoScroll()
    }

    private fun setupIndicators() {
        val container: LinearLayout = binding.indicators
        container.removeAllViews()
        for (i in bannerUrls.indices) {
            val v = View(this)
            val size = (resources.displayMetrics.density * 6).toInt()
            val lp = LinearLayout.LayoutParams(size, size)
            lp.leftMargin = (resources.displayMetrics.density * 4).toInt()
            lp.rightMargin = (resources.displayMetrics.density * 4).toInt()
            v.layoutParams = lp
            v.setBackgroundResource(R.drawable.ic_star_teal)
            container.addView(v)
        }
        updateIndicators(0)
    }

    private fun updateIndicators(pos: Int) {
        val container: LinearLayout = binding.indicators
        for (i in 0 until container.childCount) {
            val v = container.getChildAt(i)
            v.alpha = if (i == pos) 1f else 0.4f
            v.scaleX = if (i == pos) 1.6f else 1f
            v.scaleY = if (i == pos) 1.6f else 1f
        }
    }

    private fun startAutoScroll() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (autoScroll && bannerUrls.isNotEmpty()) {
                    binding.bannerViewPager.currentItem = (binding.bannerViewPager.currentItem + 1) % bannerUrls.size
                    handler.postDelayed(this, 5000)
                }
            }
        }, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        autoScroll = false
    }
}
