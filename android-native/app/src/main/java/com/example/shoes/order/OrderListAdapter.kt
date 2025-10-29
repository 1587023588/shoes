package com.example.shoes.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoes.R

class OrderListAdapter(
    private var data: List<Order>,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val thumb: ImageView = v.findViewById(R.id.thumb)
        val title: TextView = v.findViewById(R.id.title)
        val spec: TextView? = v.findViewById(R.id.spec)
        val orderNo: TextView = v.findViewById(R.id.orderNo)
        val amount: TextView = v.findViewById(R.id.amount)
        val status: TextView = v.findViewById(R.id.status)
        val time: TextView = v.findViewById(R.id.createTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val order = data[position]
        val first = order.items.firstOrNull()
        if (first?.imageUrl != null) holder.thumb.load(first.imageUrl)
        holder.title.text = first?.title ?: "共${order.totalQuantity}件商品"
        holder.spec?.text = if (first?.size.isNullOrEmpty()) "" else "尺码：${first?.size}"
        holder.orderNo.text = "订单号：${order.orderNo}"
        holder.amount.text = "¥${order.totalAmount}"
        holder.status.text = when(order.status){
            "paid" -> "待发货"
            "shipped" -> "配送中"
            "completed" -> "已完成"
            else -> order.status
        }
        holder.time.text = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date(order.createTime))
        holder.itemView.setOnClickListener { onClick(order) }
    }

    fun submit(list: List<Order>) {
        data = list
        notifyDataSetChanged()
    }
}
