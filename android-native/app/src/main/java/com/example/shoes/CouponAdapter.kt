package com.example.shoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoes.model.Coupon

class CouponAdapter(private val items: MutableList<Coupon>) : RecyclerView.Adapter<CouponAdapter.VH>() {
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvAmount: TextView = view.findViewById(com.example.shoes.R.id.tvAmount)
        val tvThreshold: TextView = view.findViewById(com.example.shoes.R.id.tvThreshold)
        val tvTitle: TextView = view.findViewById(com.example.shoes.R.id.tvTitle)
        val tvDesc: TextView = view.findViewById(com.example.shoes.R.id.tvDesc)
        val btnClaim: TextView = view.findViewById(com.example.shoes.R.id.btnClaim)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(com.example.shoes.R.layout.item_coupon, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvAmount.text = "¥${item.amount}"
        holder.tvThreshold.text = "满${item.threshold}可用"
        holder.tvTitle.text = item.title
        holder.tvDesc.text = item.desc
        bindClaim(holder, item)
        holder.btnClaim.setOnClickListener {
            if (!item.claimed) {
                item.claimed = true
                notifyItemChanged(position)
            }
        }
    }

    private fun bindClaim(holder: VH, item: Coupon) {
        if (item.claimed) {
            holder.btnClaim.text = "已领取"
            holder.btnClaim.alpha = 0.6f
            holder.btnClaim.isEnabled = false
        } else {
            holder.btnClaim.text = "立即领取"
            holder.btnClaim.alpha = 1f
            holder.btnClaim.isEnabled = true
        }
    }

    override fun getItemCount(): Int = items.size
}
