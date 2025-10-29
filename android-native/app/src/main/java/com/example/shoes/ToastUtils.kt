package com.example.shoes

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast

/**
 * 统一的 Toast 显示工具：
 * - 复用同一个 Toast 实例，避免排队导致系统抑制（queued 5 toasts）。
 * - 简单去抖：同一文案在 [debounceMs] 时间窗口内重复请求将被忽略。
 */
object ToastUtils {
    private var toast: Toast? = null
    private var lastText: CharSequence? = null
    private var lastShownAt: Long = 0L
    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    @JvmStatic
    @JvmOverloads
    fun show(
        context: Context,
        text: CharSequence?,
        duration: Int = Toast.LENGTH_SHORT,
        debounceMs: Long = 1500L
    ) {
        if (text == null || text.isEmpty()) return
        val now = SystemClock.uptimeMillis()
        val skip = (text == lastText) && (now - lastShownAt < debounceMs)
        if (skip) return

        lastText = text
        lastShownAt = now

        val appCtx = context.applicationContext
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doShow(appCtx, text, duration)
        } else {
            mainHandler.post { doShow(appCtx, text, duration) }
        }
    }

    private fun doShow(ctx: Context, text: CharSequence, duration: Int) {
        val t = toast
        if (t == null) {
            toast = Toast.makeText(ctx, text, duration)
        } else {
            t.setText(text)
            t.duration = duration
        }
        toast?.show()
    }

    @JvmStatic
    fun cancel() {
        toast?.cancel()
    }
}
