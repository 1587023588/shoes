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
import java.util.concurrent.TimeUnit

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter

    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    private var webSocket: WebSocket? = null
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5
    private val backoffBaseMillis = 1000L
    private var isShuttingDown = false

    private val room: String by lazy { intent.getStringExtra("room") ?: "public" }
    private val username: String by lazy { resolveUsername() }
    private val convName: String? by lazy { intent.getStringExtra("convName") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = convName?.let { it } ?: "群聊：$room"

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
        isShuttingDown = true
        webSocket?.close(1000, "Activity destroyed")
        client.dispatcher.executorService.shutdown()
    }

    private fun connectWebSocket() {
        if (isShuttingDown) return
        val baseWs = com.example.shoes.RemoteConfig.chatWsUrl
        val token = com.example.shoes.net.Session.token
        val userForWs = username
        val sb = StringBuilder(baseWs)
            .append("?room=")
            .append(URLEncoder.encode(room, StandardCharsets.UTF_8))
            .append("&user=")
            .append(URLEncoder.encode(userForWs, StandardCharsets.UTF_8))
        if (!token.isNullOrEmpty()) {
            sb.append("&token=").append(URLEncoder.encode(token, StandardCharsets.UTF_8))
        }
        val url = sb.toString()
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                reconnectAttempts = 0
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
                        val ts = obj.optString("timestamp", null)
                        val isSelf = user == username
                        adapter.addMessage(ChatMessage(user = user, content = content, isSelf = isSelf, timestamp = ts))
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
                scheduleReconnect()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                runOnUiThread { adapter.addSystem("连接失败：${t.message}") }
                scheduleReconnect()
            }
        })
    }

    private fun scheduleReconnect() {
        if (isShuttingDown) return
        if (reconnectAttempts >= maxReconnectAttempts || isFinishing || isDestroyed) return
        reconnectAttempts++
        val delay = (backoffBaseMillis shl (reconnectAttempts - 1)).coerceAtMost(10_000L)
        runOnUiThread { adapter.addSystem("尝试重连($reconnectAttempts/$maxReconnectAttempts)… ${delay}ms") }
        binding.root.postDelayed({ connectWebSocket() }, delay)
    }

    private fun sendCurrentText() {
        val text = binding.editMessage.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) return
        val json = JSONObject().apply {
            put("type", "message")
            put("content", text)
        }
        if (webSocket?.send(json.toString()) == true) {
            // 不进行本地乐观追加，避免与服务端广播造成重复显示，等服务端回显后再更新UI。
            binding.editMessage.text?.clear()
        } else {
            adapter.addSystem("尚未连接，无法发送")
        }
    }

    private fun defaultUserName(): String {
        val suffix = (1000..9999).random()
        return "User$suffix"
    }

    private fun resolveUsername(): String {
        // 优先从 JWT 提取，保证与后端身份一致，避免出现“两个身份”。
        val token = com.example.shoes.net.Session.token
        val fromJwt = com.example.shoes.net.JwtUtils.extractUsername(token)
        if (!fromJwt.isNullOrBlank()) return fromJwt
        // 其次采用上个页面传来的显示名
        val fromIntent = intent.getStringExtra("user")
        if (!fromIntent.isNullOrBlank()) return fromIntent
        // 兜底随机名
        return defaultUserName()
    }
}
