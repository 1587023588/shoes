package com.example.shoes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoes.databinding.ItemCategoryBinding

class CategoryDragAdapter(
    private val items: MutableList<CategoryItem>,
    private val onItemClick: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryDragAdapter.VH>() {

    class VH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon: ImageView = binding.icon
        val label: TextView = binding.label
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconRes)
        holder.label.text = item.label
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    fun swap(from: Int, to: Int) {
        if (from == to) return
        if (from !in items.indices || to !in items.indices) return
        val tmp = items[from]
        if (from < to) {
            for (i in from until to) {
                items[i] = items[i + 1]
            }
            items[to] = tmp
        } else {
            for (i in from downTo to + 1) {
                items[i] = items[i - 1]
            }
            items[to] = tmp
        }
        notifyItemMoved(from, to)
    }

    fun getItems(): List<CategoryItem> = items
}
