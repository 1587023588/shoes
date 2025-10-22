package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shoes.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 自定义底栏点击（通过嵌套 ViewBinding 直接访问子视图）
        val bottom = binding.bottomNav
        bottom.btnHome.setOnClickListener { switchTo(HomeFragment()); setSelectedTab(0) }
        bottom.btnCategory.setOnClickListener { switchTo(CategoryFragment()); setSelectedTab(1) }
    bottom.btnMessage.setOnClickListener {
        // 打开“我的会话”页面
        startActivity(android.content.Intent(this, ConversationsActivity::class.java))
        setSelectedTab(2)
    }
        bottom.btnMine.setOnClickListener { switchTo(MineFragment()); setSelectedTab(3) }
        // 默认首页
        switchTo(HomeFragment()); setSelectedTab(0)

        binding.imgPlus.setOnClickListener {
            Toast.makeText(this, "点击 +", Toast.LENGTH_SHORT).show()
        }
    }

    // 已切换使用本地 PNG 图标，避免远程加载时序导致显示异常

    private fun switchTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setSelectedTab(index: Int) {
        val labels = listOf(
            binding.bottomNav.labelHome,
            binding.bottomNav.labelCategory,
            binding.bottomNav.labelMessage,
            binding.bottomNav.labelMine
        )
        val selected = android.graphics.Color.parseColor("#B83C2A")
        val normal = android.graphics.Color.parseColor("#666666")
        labels.forEachIndexed { i, tv ->
            tv.setTextColor(if (i == index) selected else normal)
        }
    }
}
