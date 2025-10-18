package com.example.shoes

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.shoes.databinding.ActivityMuseumBinding

class MuseumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMuseumBinding
    private lateinit var pager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var autoScroll = true
    private var photos = listOf<String>()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuseumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pager = binding.viewPager

        // 从小程序迁移的图片 URL 列表（COS）- 初始化为空，下面按 1..39 生成
        photos = emptyList()

        // 生成 1..39 的 URL
        val base = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/%E4%BA%91%E6%B8%B8%E6%9D%91%E5%8F%B2%E9%A6%86/"
        photos = (1..39).map { "$base${it}.jpg" }

        pager.adapter = object : androidx.recyclerview.widget.RecyclerView.Adapter<PhotoVH>() {
            override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): PhotoVH {
                val iv = ImageView(parent.context)
                iv.layoutParams = androidx.recyclerview.widget.RecyclerView.LayoutParams(
                    androidx.recyclerview.widget.RecyclerView.LayoutParams.MATCH_PARENT,
                    androidx.recyclerview.widget.RecyclerView.LayoutParams.MATCH_PARENT
                )
                iv.scaleType = ImageView.ScaleType.FIT_CENTER
                return PhotoVH(iv)
            }

            override fun onBindViewHolder(holder: PhotoVH, position: Int) {
                holder.iv.load(photos[position]) {
                    crossfade(true)
                    placeholder(R.drawable.product_1)
                    error(R.drawable.product_1)
                }
            }

            override fun getItemCount(): Int = photos.size
        }

        // 指示器
        setupIndicators()

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        binding.btnPrev.setOnClickListener { pager.currentItem = (pager.currentItem - 1 + photos.size) % photos.size }
        binding.btnNext.setOnClickListener { pager.currentItem = (pager.currentItem + 1) % photos.size }

        binding.btnBooking.setOnClickListener {
            startActivity(android.content.Intent(this, BookingActivity::class.java))
        }

        binding.btnPlayAudio.setOnClickListener {
            // 简单播放一段示例语音（如果需要可按图片索引映射）
            playSampleAudio()
        }

        // 自动滚动
        startAutoScroll()
    }

    private fun setupIndicators() {
        binding.indicators.removeAllViews()
        for (i in photos.indices) {
            val v = View(this)
            val size = resources.displayMetrics.density * 6
            val lp = LinearLayout.LayoutParams(size.toInt(), size.toInt())
            lp.leftMargin = (resources.displayMetrics.density * 4).toInt()
            lp.rightMargin = (resources.displayMetrics.density * 4).toInt()
            v.layoutParams = lp
            v.setBackgroundResource(R.drawable.ic_star_teal)
            binding.indicators.addView(v)
        }
        updateIndicators(0)
    }

    private fun updateIndicators(pos: Int) {
        for (i in 0 until binding.indicators.childCount) {
            val v = binding.indicators.getChildAt(i)
            v.alpha = if (i == pos) 1f else 0.4f
            v.scaleX = if (i == pos) 1.6f else 1f
            v.scaleY = if (i == pos) 1.6f else 1f
        }
    }

    private fun startAutoScroll() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (autoScroll) {
                    pager.currentItem = (pager.currentItem + 1) % photos.size
                    handler.postDelayed(this, 5000)
                }
            }
        }, 5000)
    }

    private fun playSampleAudio() {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer()
        val url = "https://shoes-1379330878.cos.ap-beijing.myqcloud.com/audio/1.mp3"
        try {
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(this, Uri.parse(url))
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener { it.start() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    class PhotoVH(val iv: ImageView) : androidx.recyclerview.widget.RecyclerView.ViewHolder(iv)
}
