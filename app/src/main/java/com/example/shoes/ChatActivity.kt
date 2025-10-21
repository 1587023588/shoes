package com.example.shoes

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoes.databinding.ActivityChatBinding
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val room: String by lazy { intent.getStringExtra("room") ?: "public" }
    private val username: String by lazy { intent.getStringExtra("user") ?: defaultUserName() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "群聊：$room"

        adapter = ChatAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        binding.recyclerView.adapter = adapter

        binding.btnSend.setOnClickListener { sendCurrentText() }
        binding.editMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendCurrentText(); true
            } else false
        }

        connectWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "Activity destroyed")
        client.dispatcher.executorService.shutdown()
    }

    private fun connectWebSocket() {
        // 使用 Android 模拟器访问宿主机后端
        val baseWs = "ws://10.0.2.2:8080/ws/chat"
        val url = baseWs + "?room=" + URLEncoder.encode(room, StandardCharsets.UTF_8) +
                "&user=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                runOnUiThread { adapter.addSystem("已连接到房间：$room 作为 $username") }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val obj = try { JSONObject(text) } catch (_: Exception) { null }
                val type = obj?.optString("type")
                when (type) {
                    "system" -> runOnUiThread { adapter.addSystem(obj.optString("content")) }
                    "message" -> runOnUiThread {
                        val user = obj.optString("user", "?")
                        val content = obj.optString("content", "")
                        adapter.addMessage(ChatMessage(user, content))
                        binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                    }
                    else -> runOnUiThread { adapter.addSystem(text) }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 忽略二进制消息
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                runOnUiThread { adapter.addSystem("连接关闭：$reason ($code)") }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                runOnUiThread { adapter.addSystem("连接失败：${t.message}") }
            }
        })
    }

    private fun sendCurrentText() {
        val text = binding.editMessage.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) return
        val json = JSONObject().apply {
            put("type", "message")
            put("content", text)
        }
        if (webSocket?.send(json.toString()) == true) {
            adapter.addMessage(ChatMessage(username, text))
            binding.editMessage.text?.clear()
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        } else {
            adapter.addSystem("尚未连接，无法发送")
        }
    }

    private fun defaultUserName(): String {
        val suffix = (1000..9999).random()
        return "User$suffix"
    }
}
