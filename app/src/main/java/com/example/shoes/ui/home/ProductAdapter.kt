package com.example.shoes.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoes.R
import com.example.shoes.databinding.ItemProductBinding
import com.example.shoes.model.Product

class ProductAdapter(
    private val items: List<Product>,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    class VH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.title.text = item.name
        holder.binding.price.text = "¥${item.price}"
        holder.binding.stock.text = if (item.stock <= 5) "库存紧张(${item.stock})" else "库存：${item.stock}"
        // 始终使用本地商品图 goods_1..goods_10（来自 drawable-nodpi 根目录），超出范围时循环
        val goodsImages = listOf(
            R.drawable.goods_1,
            R.drawable.goods_2,
            R.drawable.goods_3,
            R.drawable.goods_4,
            R.drawable.goods_5,
            R.drawable.goods_6,
            R.drawable.goods_7,
            R.drawable.goods_8,
            R.drawable.goods_9,
            R.drawable.goods_10,
        )
        val resId = goodsImages[position % goodsImages.size]
        holder.binding.cover.setImageResource(resId)
        holder.binding.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
