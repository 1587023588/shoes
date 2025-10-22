package com.example.shoes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoes.model.VillageActivity

class VillageActivityAdapter(private val items: List<VillageActivity>) : RecyclerView.Adapter<VillageActivityAdapter.VH>() {
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvBrief: TextView = view.findViewById(R.id.tvBrief)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_village_activity, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvTime.text = "时间：${item.time}"
        holder.tvLocation.text = "地点：${item.location}"
        holder.tvBrief.text = item.brief
        holder.tvStatus.text = item.status
        holder.tvStatus.setBackgroundColor(
            when (item.status) {
                "报名中" -> Color.parseColor("#4A7463")
                "进行中" -> Color.parseColor("#FF9800")
                else -> Color.parseColor("#9E9E9E")
            }
        )
    }

    override fun getItemCount(): Int = items.size
}
