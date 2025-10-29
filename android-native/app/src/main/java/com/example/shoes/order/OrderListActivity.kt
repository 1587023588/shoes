package com.example.shoes.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoes.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderListBinding
    private lateinit var adapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filter = intent.getStringExtra(EXTRA_STATUS) // 可选：paid/shipped/completed

        adapter = OrderListAdapter(emptyList()) { order ->
            startActivity(OrderDetailActivity.intent(this, order.orderNo))
        }
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter

        refresh(filter)
        binding.swipe.setOnRefreshListener { refresh(filter) }
    }

    private fun refresh(filter: String?) {
        val all = LocalOrderStore.get(this).list()
        val list = if (filter.isNullOrEmpty()) all else all.filter { it.status == filter }
        adapter.submit(list)
        binding.empty.visibility = if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.swipe.isRefreshing = false
    }

    companion object {
        const val EXTRA_STATUS = "status"
    }
}
