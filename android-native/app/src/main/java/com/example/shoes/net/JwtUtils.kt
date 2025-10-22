package com.example.shoes.net

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    /**
     * 从 JWT 中提取用户名，优先顺序：preferred_username > username > name > sub
     */
    fun extractUsername(token: String?): String? {
        if (token.isNullOrBlank()) return null
        val parts = token.split('.')
        if (parts.size < 2) return null
        return try {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            val obj = JSONObject(payload)
            obj.optString("preferred_username").takeIf { it.isNotBlank() }
                ?: obj.optString("username").takeIf { it.isNotBlank() }
                ?: obj.optString("name").takeIf { it.isNotBlank() }
                ?: obj.optString("sub").takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }
}
