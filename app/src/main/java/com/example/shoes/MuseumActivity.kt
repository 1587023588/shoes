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
    data class ExhibitionItem(val title: String, val resId: Int)

    private lateinit var binding: ActivityMuseumBinding
    private lateinit var pager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var autoScroll = true
    private var exhibitionItems = listOf<ExhibitionItem>()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuseumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pager = binding.viewPager

        // 云上展馆使用 m1..m33 本地图片
        exhibitionItems = listOf(
            ExhibitionItem("纪念展项 1", R.drawable.m1),
            ExhibitionItem("纪念展项 2", R.drawable.m2),
            ExhibitionItem("纪念展项 3", R.drawable.m3),
            ExhibitionItem("纪念展项 4", R.drawable.m4),
            ExhibitionItem("纪念展项 5", R.drawable.m5),
            ExhibitionItem("纪念展项 6", R.drawable.m6),
            ExhibitionItem("纪念展项 7", R.drawable.m7),
            ExhibitionItem("纪念展项 8", R.drawable.m8),
            ExhibitionItem("纪念展项 9", R.drawable.m9),
            ExhibitionItem("纪念展项 10", R.drawable.m10),
            ExhibitionItem("纪念展项 11", R.drawable.m11),
            ExhibitionItem("纪念展项 12", R.drawable.m12),
            ExhibitionItem("纪念展项 13", R.drawable.m13),
            ExhibitionItem("纪念展项 14", R.drawable.m14),
            ExhibitionItem("纪念展项 15", R.drawable.m15),
            ExhibitionItem("纪念展项 16", R.drawable.m16),
            ExhibitionItem("纪念展项 17", R.drawable.m17),
            ExhibitionItem("纪念展项 18", R.drawable.m18),
            ExhibitionItem("纪念展项 19", R.drawable.m19),
            ExhibitionItem("纪念展项 20", R.drawable.m20),
            ExhibitionItem("纪念展项 21", R.drawable.m21),
            ExhibitionItem("纪念展项 22", R.drawable.m22),
            ExhibitionItem("纪念展项 23", R.drawable.m23),
            ExhibitionItem("纪念展项 24", R.drawable.m24),
            ExhibitionItem("纪念展项 25", R.drawable.m25),
            ExhibitionItem("纪念展项 26", R.drawable.m26),
            ExhibitionItem("纪念展项 27", R.drawable.m27),
            ExhibitionItem("纪念展项 28", R.drawable.m28),
            ExhibitionItem("纪念展项 29", R.drawable.m29),
            ExhibitionItem("纪念展项 30", R.drawable.m30),
            ExhibitionItem("纪念展项 31", R.drawable.m31),
            ExhibitionItem("纪念展项 32", R.drawable.m32),
            ExhibitionItem("纪念展项 33", R.drawable.m33)
        )

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
                holder.iv.setImageResource(exhibitionItems[position].resId)
            }

            override fun getItemCount(): Int = exhibitionItems.size
        }

        // 指示器
        setupIndicators()

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
                binding.tvPhotoTitle.text = exhibitionItems[position].title
            }
        })

        binding.btnPrev.setOnClickListener { pager.currentItem = (pager.currentItem - 1 + exhibitionItems.size) % exhibitionItems.size }
        binding.btnNext.setOnClickListener { pager.currentItem = (pager.currentItem + 1) % exhibitionItems.size }

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
        for (i in exhibitionItems.indices) {
            val v = View(this)
            val size = resources.displayMetrics.density * 5 // Reduced size to fit 36 items
            val lp = LinearLayout.LayoutParams(size.toInt(), size.toInt())
            lp.leftMargin = (resources.displayMetrics.density * 2).toInt() // Reduced margin
            lp.rightMargin = (resources.displayMetrics.density * 2).toInt()
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
                if (autoScroll && exhibitionItems.isNotEmpty()) {
                    pager.currentItem = (pager.currentItem + 1) % exhibitionItems.size
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
