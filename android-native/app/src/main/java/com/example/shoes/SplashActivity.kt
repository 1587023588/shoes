package com.example.shoes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.shoes.net.Session

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.decorView.postDelayed({
            // 检查本地是否有缓存的 Token
            val sp = getSharedPreferences("session", MODE_PRIVATE)
            
            // 【调试】强制清除旧的 Token，确保能看到登录页
            sp.edit().remove("auth_token").apply()
            
            val token = sp.getString("auth_token", null)

            if (!token.isNullOrEmpty()) {
                // 有 Token -> 恢复 Session 并进入主页
                Session.token = token
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // 无 Token -> 进入登录页
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1200)
    }
}
