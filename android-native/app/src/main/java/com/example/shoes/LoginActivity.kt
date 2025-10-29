package com.example.shoes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoes.databinding.ActivityLoginBinding
import com.example.shoes.data.RemoteRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val repo = RemoteRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val u = binding.editUsername.text?.toString()?.trim().orEmpty()
            val p = binding.editPassword.text?.toString()?.trim().orEmpty()
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progress.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false
            lifecycleScope.launch {
                try {
                    // 避免网络不通时长时间无响应，增加 5s 超时
                    kotlinx.coroutines.withTimeout(5000L) {
                        repo.login(u, p)
                    }
                    Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } catch (e: Exception) {
                    val msg = when (e) {
                        is kotlinx.coroutines.TimeoutCancellationException -> "服务器连接超时，请稍后再试"
                        else -> e.message ?: "登录失败"
                    }
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                } finally {
                    binding.progress.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                }
            }
        }

        binding.btnClose.setOnClickListener { finish() }
    }
}
