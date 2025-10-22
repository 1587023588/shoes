package com.example.shoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoes.databinding.FragmentMessageBinding
import okhttp3.*
import okio.ByteString

class SimpleWsListener(
    private val onLog: (String) -> Unit
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        onLog("[open] ${response.code}")
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        onLog("[msg] $text")
    }
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        onLog("[msg] bytes ${bytes.size}")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        onLog("[closing] $code $reason")
        webSocket.close(1000, null)
    }
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        onLog("[closed] $code $reason")
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        onLog("[error] ${t.message}")
    }
}

/**
 * 消息/聊天室占位页面：先提供入口与简单占位，后续可接入后端 WebSocket/IM。
 */
class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private var client: OkHttpClient? = null
    private var ws: WebSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)

        fun appendLog(msg: String) {
            val old = binding.tvLog.text?.toString() ?: ""
            binding.tvLog.text = old + if (old.isEmpty()) msg else "\n" + msg
        }

        binding.btnConnect.setOnClickListener {
            if (ws != null) return@setOnClickListener
            client = OkHttpClient()
            val req = Request.Builder().url(RemoteConfig.chatWsUrl).build()
            ws = client!!.newWebSocket(req, SimpleWsListener { appendLog(it) })
            appendLog("Connecting to ${RemoteConfig.chatWsUrl} ...")
        }

        binding.btnDisconnect.setOnClickListener {
            ws?.close(1000, "bye")
            ws = null
            client?.dispatcher?.executorService?.shutdown()
            client = null
            appendLog("Disconnected")
        }

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text?.toString()?.trim().orEmpty()
            if (text.isNotEmpty()) {
                val ok = ws?.send(text) ?: false
                appendLog(if (ok) "[send] $text" else "[send failed]")
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { ws?.close(1000, null) } catch (_: Throwable) {}
        ws = null
        try { client?.dispatcher?.executorService?.shutdown() } catch (_: Throwable) {}
        client = null
        _binding = null
    }
}
