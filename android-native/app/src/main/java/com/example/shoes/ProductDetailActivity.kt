package com.example.shoes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.data.ProductRepository
import com.example.shoes.databinding.ActivityProductDetailBinding
import android.view.View
import android.widget.ImageView
import coil.load

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private var selectedCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(KEY_ID)
        val product = id?.let { ProductRepository.getById(it) }
        if (product == null) {
            finish()
            return
        }
        binding.title.text = product.name
        binding.price.text = "¥${product.price}"
        if (product.oldPrice != null) {
            binding.oldPrice.visibility = View.VISIBLE
            binding.oldPrice.text = "¥${product.oldPrice}"
            binding.oldPrice.paintFlags = binding.oldPrice.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.oldPrice.visibility = View.GONE
        }
        binding.intro.text = product.intro ?: ""
        // 简易“已售”占位
        binding.soldNum.text = "已售${(100..500).random()}"

        // 绑定图集（优先远程 URL，确保与小程序一致；否则回退本地）
        val gallery = binding.gallery
        gallery.removeAllViews()
        val lp = android.widget.LinearLayout.LayoutParams(
            resources.displayMetrics.widthPixels,
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT
        )
        val galleryUrls = if (product.imagesUrls.isNotEmpty()) product.imagesUrls else emptyList()
        if (galleryUrls.isNotEmpty()) {
            galleryUrls.forEach { url ->
                val iv = ImageView(this)
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                iv.load(url)
                gallery.addView(iv, lp)
            }
        } else {
            product.images.forEach { resId ->
                val iv = ImageView(this)
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                iv.setImageResource(resId)
                gallery.addView(iv, lp)
            }
        }

        // 详情图片（优先远程 URL）
        val descList = binding.descList
        descList.removeAllViews()
        val detailUrls = if (product.descImageUrls.isNotEmpty()) product.descImageUrls else emptyList()
        if (detailUrls.isNotEmpty()) {
            detailUrls.forEach { url ->
                val iv = ImageView(this)
                iv.adjustViewBounds = true
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                iv.load(url)
                val p = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                p.topMargin = (8 * resources.displayMetrics.density).toInt()
                descList.addView(iv, p)
            }
        } else {
            product.descImages.forEach { resId ->
                val iv = ImageView(this)
                iv.adjustViewBounds = true
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                iv.setImageResource(resId)
                val p = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                p.topMargin = (8 * resources.displayMetrics.density).toInt()
                descList.addView(iv, p)
            }
        }

        // 已选/规格面板
        binding.rowSelected.setOnClickListener {
            toggleSpecSheet(true, product)
        }
        binding.btnSpecMinus.setOnClickListener {
            if (selectedCount > 1) selectedCount--
            binding.specCount.text = selectedCount.toString()
        }
        binding.btnSpecPlus.setOnClickListener {
            selectedCount++
            binding.specCount.text = selectedCount.toString()
        }
        binding.btnSpecConfirm.setOnClickListener {
            binding.selectedText.text = "${selectedCount}件  尺码:${product.sizeRange ?: "-"}"
            toggleSpecSheet(false, product)
        }

        // 购买栏
        binding.btnAdd.setOnClickListener {
            ShoppingCart.add(product.id, selectedCount)
            binding.btnAdd.text = "已加入"
        }
        binding.btnBuy.setOnClickListener {
            ShoppingCart.add(product.id, selectedCount)
            // 可跳转到订单确认页（暂空）
        }
    }

    private fun toggleSpecSheet(show: Boolean, product: com.example.shoes.model.Product) {
        binding.specTitle.text = "选择规格"
        binding.specHint.text = "尺码：${product.sizeRange ?: "-"}"
        binding.specSheet.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        private const val KEY_ID = "id"
        fun intent(ctx: Context, id: String): Intent = Intent(ctx, ProductDetailActivity::class.java).apply {
            putExtra(KEY_ID, id)
        }
    }
}
