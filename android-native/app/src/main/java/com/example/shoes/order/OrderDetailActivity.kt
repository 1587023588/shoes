package com.example.shoes.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.databinding.ActivityOrderDetailBinding
import coil.load

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderNo = intent.getStringExtra(KEY_ORDER_NO) ?: run { finish(); return }
        val order = LocalOrderStore.get(this).get(orderNo) ?: run { finish(); return }

        // 收货信息（ViewBinding 下为非空视图，直接赋值即可）
        binding.consignee.text = "收货人：" + (order.consigneeName ?: "-")
        binding.phone.text = "电话：" + (order.consigneePhone ?: "-")
        binding.address.text = "地址：" + (order.address ?: "-")

        binding.orderNo.text = order.orderNo
        binding.createTime.text = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date(order.createTime))
        binding.status.text = when(order.status){
            "paid" -> "待发货"
            "shipped" -> "配送中"
            "completed" -> "已完成"
            else -> order.status
        }
        binding.total.text = "¥${order.totalAmount}（共${order.totalQuantity}件）"

        val first = order.items.firstOrNull()
        if (first != null) {
            binding.title.text = first.title
            // 显示尺码（如有）
            binding.spec.text = if (!first.size.isNullOrEmpty()) "尺码：${first.size}" else ""
            binding.price.text = "¥${first.price} x ${first.quantity}"
            if (!first.imageUrl.isNullOrEmpty()) binding.thumb.load(first.imageUrl)
        }
    }

    companion object {
        private const val KEY_ORDER_NO = "orderNo"
        fun intent(ctx: Context, orderNo: String): Intent =
            Intent(ctx, OrderDetailActivity::class.java).apply { putExtra(KEY_ORDER_NO, orderNo) }
    }
}
