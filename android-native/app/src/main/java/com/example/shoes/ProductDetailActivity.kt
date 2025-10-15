package com.example.shoes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.data.ProductRepository
import com.example.shoes.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding

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
        binding.desc.text = "工艺：${product.craft ?: "-"}\n材质：${product.material ?: "-"}\n尺码：${product.sizeRange ?: "-"}"
        binding.btnAdd.setOnClickListener {
            ShoppingCart.add(product.id)
            binding.btnAdd.isEnabled = false
            binding.btnAdd.text = "已加入"
        }
    }

    companion object {
        private const val KEY_ID = "id"
        fun intent(ctx: Context, id: String): Intent = Intent(ctx, ProductDetailActivity::class.java).apply {
            putExtra(KEY_ID, id)
        }
    }
}
