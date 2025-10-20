package com.example.shoes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoes.databinding.ItemBannerImageBinding

/**
 * 简单的图片轮播适配器（ViewPager2 使用）。
 * - 优先加载提供的远程 URL
 * - 加载失败时尝试 jpg/png 扩展名互换
 * - 最终兜底到本地占位图
 */
class BannerAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<BannerAdapter.VH>() {

    class VH(val binding: ItemBannerImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBannerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val url = images[position]
        val iv: ImageView = holder.binding.image
        iv.scaleType = ImageView.ScaleType.CENTER_CROP

        // 一次加载：失败时尝试互换扩展名
        iv.load(url) {
            crossfade(true)
            placeholder(R.drawable.goods_1)
            error(R.drawable.goods_1)
            listener(
                onError = { _, _ ->
                    // 尝试 jpg <-> png 互换
                    val alt = when {
                        url.endsWith(".jpg", ignoreCase = true) -> url.replace(Regex("\\.jpg$", RegexOption.IGNORE_CASE), ".png")
                        url.endsWith(".png", ignoreCase = true) -> url.replace(Regex("\\.png$", RegexOption.IGNORE_CASE), ".jpg")
                        else -> null
                    }
                    if (!alt.isNullOrEmpty()) {
                        iv.load(alt) {
                            crossfade(true)
                            placeholder(R.drawable.goods_1)
                            error(R.drawable.goods_1)
                        }
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int = images.size
}
