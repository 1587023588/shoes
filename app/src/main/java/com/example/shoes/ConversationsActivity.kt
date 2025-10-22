package com.example.shoes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoes.data.ChatRepository
import com.example.shoes.databinding.ActivityConversationsBinding
import com.example.shoes.net.Session
import com.example.shoes.net.ChatConversationDto
import kotlinx.coroutines.launch

class ConversationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConversationsBinding
    private val repo = ChatRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "我的会话"

        val lm = LinearLayoutManager(this)
        binding.recycler.layoutManager = lm
        binding.recycler.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        val adapter = ConversationsAdapter { conv ->
            // 打开 ChatActivity，使用会话ID
            val it = Intent(this, ChatActivity::class.java)
            it.putExtra("room", conv.id.toString())
            it.putExtra("convName", conv.name ?: (conv.type ?: "会话") + "#" + conv.id)
            startActivity(it)
        }
        binding.recycler.adapter = adapter

        binding.swipe.setOnRefreshListener { loadConversations(adapter) }

        if (Session.token.isNullOrEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
        }
        loadConversations(adapter)
    }

    private fun loadConversations(adapter: ConversationsAdapter) {
        binding.swipe.isRefreshing = true
        binding.empty.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val list = repo.listConversations()
                adapter.submit(list)
                binding.empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                Toast.makeText(this@ConversationsActivity, e.message ?: "加载失败", Toast.LENGTH_SHORT).show()
            } finally {
                binding.swipe.isRefreshing = false
            }
        }
    }
}

private class ConversationsAdapter(
    val onClick: (ChatConversationDto) -> Unit
) : RecyclerView.Adapter<ConversationsVH>() {
    private val data = mutableListOf<ChatConversationDto>()
    fun submit(list: List<ChatConversationDto>) {
        data.clear(); data.addAll(list); notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ConversationsVH {
        val inflater = android.view.LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_conversation, parent, false)
        return ConversationsVH(view)
    }
    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ConversationsVH, position: Int) {
        holder.bind(data[position], onClick)
    }
}

private class ConversationsVH(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    private val title = itemView.findViewById<android.widget.TextView>(R.id.title)
    private val subtitle = itemView.findViewById<android.widget.TextView>(R.id.subtitle)
    fun bind(item: ChatConversationDto, onClick: (ChatConversationDto) -> Unit) {
        title.text = item.name ?: (item.type ?: "会话") + "#" + item.id
        subtitle.text = when(item.type) { "DM" -> "私聊"; "GROUP" -> "群聊"; else -> (item.type ?: "") }
        itemView.setOnClickListener { onClick(item) }
    }
}
