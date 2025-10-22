package com.example.shoes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoes.databinding.ItemMessageBinding

data class ChatMessage(val user: String, val content: String, val system: Boolean = false)

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.Holder>() {
    private val items = mutableListOf<ChatMessage>()

    fun addMessage(msg: ChatMessage) {
        items.add(msg)
        notifyItemInserted(items.lastIndex)
    }

    fun addSystem(text: String) {
        addMessage(ChatMessage(user = "系统", content = text, system = true))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val m = items[position]
        holder.binding.txtUser.text = if (m.system) "[系统]" else m.user
        holder.binding.txtContent.text = m.content
        holder.binding.txtContent.alpha = if (m.system) 0.7f else 1f
    }

    override fun getItemCount(): Int = items.size

    class Holder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)
}
