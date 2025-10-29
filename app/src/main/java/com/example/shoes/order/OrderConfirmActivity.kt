package com.example.shoes.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.databinding.ActivityOrderConfirmBinding
import com.example.shoes.data.ProductRepository
import coil.load
import com.example.shoes.ToastUtils

class OrderConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderConfirmBinding
    private var count: Int = 1
    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getStringExtra(KEY_ID) ?: run {
            finish(); return
        }
        count = intent.getIntExtra(KEY_COUNT, 1).coerceAtLeast(1)

        val product = ProductRepository.getById(productId)
        if (product == null) { finish(); return }

        binding.title.text = product.name
        binding.price.text = "¥${product.price}"
        binding.count.text = count.toString()
        val imgUrl = product.primaryImageUrl ?: product.imagesUrls.firstOrNull()
        if (imgUrl != null) {
            binding.thumb.load(imgUrl)
        } else if (product.images.isNotEmpty()) {
            binding.thumb.setImageResource(product.images.first())
        }

        fun refreshTotal() {
            val total = product.price * count
            binding.total.text = "合计：¥$total"
        }
        refreshTotal()

        binding.btnMinus.setOnClickListener { if (count > 1) { count--; binding.count.text = count.toString(); refreshTotal() } }
        binding.btnPlus.setOnClickListener { count++; binding.count.text = count.toString(); refreshTotal() }
        binding.btnSubmit.setOnClickListener {
            val order = LocalOrderStore.get(this).create(listOf(
                OrderItem(
                    productId = product.id,
                    title = product.name,
                    price = product.price,
                    quantity = count,
                    imageUrl = imgUrl
                )
            ))
            ToastUtils.show(this, "订单已创建")
            startActivity(OrderDetailActivity.intent(this, order.orderNo))
            finish()
        }
    }

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_COUNT = "count"
        fun intent(ctx: Context, productId: String, count: Int = 1): Intent =
            Intent(ctx, OrderConfirmActivity::class.java).apply {
                putExtra(KEY_ID, productId)
                putExtra(KEY_COUNT, count)
            }
    }
}
