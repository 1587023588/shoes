package com.example.shoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ChatMessage(
    val user: String?,
    val content: String,
    val system: Boolean = false,
    val isSelf: Boolean = false,
    val timestamp: String? = null
)

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ChatMessage>()

    fun addMessage(msg: ChatMessage) {
        items.add(msg)
        notifyItemInserted(items.lastIndex)
    }

    fun addSystem(text: String) {
        addMessage(ChatMessage(user = "系统", content = text, system = true))
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        val m = items[position]
        return when {
            m.system -> VIEW_SYSTEM
            m.isSelf -> VIEW_RIGHT
            else -> VIEW_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_SYSTEM -> SystemVH(inflater.inflate(R.layout.item_message_system, parent, false))
            VIEW_RIGHT -> RightVH(inflater.inflate(R.layout.item_message_right, parent, false))
            else -> LeftVH(inflater.inflate(R.layout.item_message_left, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val m = items[position]
        when (holder) {
            is SystemVH -> holder.bind(m)
            is LeftVH -> holder.bind(m)
            is RightVH -> holder.bind(m)
        }
    }

    private class SystemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)
        fun bind(m: ChatMessage) {
            text.text = m.content
        }
    }

    private class LeftVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)
        private val time: TextView? = itemView.findViewById(R.id.time)
        private val name: TextView? = itemView.findViewById(R.id.name)
        fun bind(m: ChatMessage) {
            text.text = m.content
            time?.text = m.timestamp?.replace('T', ' ')?.replace("Z", "")
            name?.text = m.user ?: ""
        }
    }

    private class RightVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)
        private val time: TextView? = itemView.findViewById(R.id.time)
        private val name: TextView? = itemView.findViewById(R.id.name)
        fun bind(m: ChatMessage) {
            text.text = m.content
            time?.text = m.timestamp?.replace('T', ' ')?.replace("Z", "")
            name?.text = "我"
        }
    }

    companion object {
        private const val VIEW_SYSTEM = 0
        private const val VIEW_LEFT = 1
        private const val VIEW_RIGHT = 2
    }
}
