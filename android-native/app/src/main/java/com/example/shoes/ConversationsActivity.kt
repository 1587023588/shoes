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
import com.example.shoes.net.ChatConversationDto
import com.example.shoes.net.Session
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
        val adapter = ConversationsAdapter({ conv ->
            val it = Intent(this, ChatActivity::class.java)
            it.putExtra("room", conv.id.toString())
            it.putExtra("convName", conv.name ?: (conv.type ?: "会话") + "#" + conv.id)
            startActivity(it)
        }, { conv ->
            // 长按：退出/删除会话
            val options = arrayOf("退出/删除会话", "强制删除(群主)")
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(if (conv.type == "GROUP") "群聊操作" else "会话操作")
                .setItems(options) { d, which ->
                    val force = (which == 1)
                    binding.swipe.isRefreshing = true
                    lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                        try {
                            repo.deleteConversation(conv.id, if (force) true else null)
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                val msg = if (force) "已删除群聊" else "已退出/删除"
                                Toast.makeText(this@ConversationsActivity, msg, Toast.LENGTH_SHORT).show()
                                loadConversations(binding.recycler.adapter as ConversationsAdapter)
                            }
                        } catch (e: Exception) {
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                Toast.makeText(this@ConversationsActivity, e.message ?: "操作失败", Toast.LENGTH_SHORT).show()
                                binding.swipe.isRefreshing = false
                            }
                        }
                    }
                    d.dismiss()
                }
                .setNegativeButton("取消", null)
                .show()
            true
        })
        binding.recycler.adapter = adapter

        binding.swipe.setOnRefreshListener { loadConversations(adapter) }

        if (Session.token.isNullOrEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
        }
        loadConversations(adapter)

        // 空态：快速创建会话
        binding.btnCreateGroup.setOnClickListener {
            if (Session.token.isNullOrEmpty()) {
                Toast.makeText(this, "请先登录后再创建群组", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            binding.swipe.isRefreshing = true
            lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val createdId = repo.createGroup("测试群聊", emptyList())
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        // 新建成功后直接进入聊天
                        openChatById(createdId, null)
                        loadConversations(adapter)
                    }
                } catch (e: Exception) {
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        Toast.makeText(this@ConversationsActivity, e.message ?: "创建失败", Toast.LENGTH_SHORT).show()
                        binding.swipe.isRefreshing = false
                    }
                }
            }
        }
        binding.btnCreateDm.setOnClickListener {
            if (Session.token.isNullOrEmpty()) {
                Toast.makeText(this, "请先登录后再发起私聊", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            // 简易输入框，输入对方用户ID
            val input = android.widget.EditText(this).apply {
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                hint = "输入对方用户ID"
            }
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("发起私聊")
                .setView(input)
                .setPositiveButton("确定") { d, _ ->
                    val id = input.text?.toString()?.trim()?.toLongOrNull()
                    if (id == null) {
                        Toast.makeText(this, "请输入有效的用户ID", Toast.LENGTH_SHORT).show(); return@setPositiveButton
                    }
                    binding.swipe.isRefreshing = true
                    lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                        try {
                            val convId = repo.ensureDm(id)
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                openChatById(convId, null)
                                loadConversations(adapter)
                            }
                        } catch (e: Exception) {
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                Toast.makeText(this@ConversationsActivity, e.message ?: "创建失败", Toast.LENGTH_SHORT).show()
                                binding.swipe.isRefreshing = false
                            }
                        }
                    }
                    d.dismiss()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_conversations, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_group -> {
                if (Session.token.isNullOrEmpty()) {
                    Toast.makeText(this, "请先登录后再创建群组", Toast.LENGTH_SHORT).show(); return true
                }
                val input = android.widget.EditText(this).apply {
                    hint = "输入群组名称"
                    setText("测试群聊")
                }
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("创建群组")
                    .setView(input)
                    .setPositiveButton("创建") { d, _ ->
                        val name = input.text?.toString()?.ifBlank { "测试群聊" } ?: "测试群聊"
                        binding.swipe.isRefreshing = true
                        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                            try {
                                val created = repo.createGroup(name, emptyList())
                                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                    openChatById(created, name.ifBlank { null })
                                    loadConversations(binding.recycler.adapter as ConversationsAdapter)
                                }
                            } catch (e: Exception) {
                                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                    Toast.makeText(this@ConversationsActivity, e.message ?: "创建失败", Toast.LENGTH_SHORT).show()
                                    binding.swipe.isRefreshing = false
                                }
                            }
                        }
                        d.dismiss()
                    }
                    .setNegativeButton("取消", null)
                    .show()
                return true
            }
            R.id.action_create_dm -> {
                if (Session.token.isNullOrEmpty()) {
                    Toast.makeText(this, "请先登录后再发起私聊", Toast.LENGTH_SHORT).show(); return true
                }
                val input = android.widget.EditText(this).apply {
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER
                    hint = "输入对方用户ID"
                }
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("发起私聊")
                    .setView(input)
                    .setPositiveButton("确定") { d, _ ->
                        val id = input.text?.toString()?.trim()?.toLongOrNull()
                        if (id == null) {
                            Toast.makeText(this, "请输入有效的用户ID", Toast.LENGTH_SHORT).show(); return@setPositiveButton
                        }
                        binding.swipe.isRefreshing = true
                        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                            try {
                                val conv = repo.ensureDm(id)
                                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                    openChatById(conv, null)
                                    loadConversations(binding.recycler.adapter as ConversationsAdapter)
                                }
                            } catch (e: Exception) {
                                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                    Toast.makeText(this@ConversationsActivity, e.message ?: "创建失败", Toast.LENGTH_SHORT).show()
                                    binding.swipe.isRefreshing = false
                                }
                            }
                        }
                        d.dismiss()
                    }
                    .setNegativeButton("取消", null)
                    .show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadConversations(adapter: ConversationsAdapter) {
        binding.swipe.isRefreshing = true
        binding.empty.visibility = View.GONE
        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val list = repo.listConversations()
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    adapter.submit(list)
                    binding.empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    Toast.makeText(this@ConversationsActivity, e.message ?: "加载失败", Toast.LENGTH_SHORT).show()
                    // 出错也展示空态，避免整页纯白
                    binding.empty.visibility = View.VISIBLE
                }
            } finally {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    binding.swipe.isRefreshing = false
                }
            }
        }
    }
}

private class ConversationsAdapter(
    val onClick: (ChatConversationDto) -> Unit,
    val onLongClick: (ChatConversationDto) -> Boolean
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
        holder.bind(data[position], onClick, onLongClick)
    }
}

private class ConversationsVH(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    private val title = itemView.findViewById<android.widget.TextView>(R.id.title)
    private val subtitle = itemView.findViewById<android.widget.TextView>(R.id.subtitle)
    fun bind(item: ChatConversationDto, onClick: (ChatConversationDto) -> Unit, onLongClick: (ChatConversationDto) -> Boolean) {
        title.text = item.name ?: (item.type ?: "会话") + "#" + item.id
        subtitle.text = when(item.type) { "DM" -> "私聊"; "GROUP" -> "群聊"; else -> (item.type ?: "") }
        itemView.setOnClickListener { onClick(item) }
        itemView.setOnLongClickListener { onLongClick(item) }
    }
}

private fun ConversationsActivity.openChatById(conversationId: Long, name: String?) {
    val it = Intent(this, ChatActivity::class.java)
    it.putExtra("room", conversationId.toString())
    it.putExtra("convName", name ?: ("会话#" + conversationId))
    startActivity(it)
}
