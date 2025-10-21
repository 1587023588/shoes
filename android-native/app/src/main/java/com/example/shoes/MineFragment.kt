package com.example.shoes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoes.databinding.FragmentMineBinding
import com.example.shoes.net.Session

class MineFragment : Fragment() {
    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!
    private val reqLogin = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)

    // 占位交互
    binding.btnSettings.setOnClickListener { Toast.makeText(requireContext(), "打开设置", Toast.LENGTH_SHORT).show() }

        // 订单分组点击
        binding.orderWaitPay.setOnClickListener { Toast.makeText(requireContext(), "待付款订单", Toast.LENGTH_SHORT).show() }
        binding.orderWaitShip.setOnClickListener { Toast.makeText(requireContext(), "待发货订单", Toast.LENGTH_SHORT).show() }
        binding.orderWaitReceive.setOnClickListener { Toast.makeText(requireContext(), "待收货订单", Toast.LENGTH_SHORT).show() }
        binding.orderAll.setOnClickListener { Toast.makeText(requireContext(), "全部订单", Toast.LENGTH_SHORT).show() }

        // 用户卡片点击 -> 去登录
        binding.userCard.setOnClickListener {
            if (Session.token == null) {
                startActivityForResult(Intent(requireContext(), LoginActivity::class.java), reqLogin)
            } else {
                Toast.makeText(requireContext(), "已登录", Toast.LENGTH_SHORT).show()
            }
        }

        renderLoginState()

        // 版本号
        try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            val versionName = pInfo.versionName ?: ""
            if (versionName.isNotEmpty()) {
                binding.versionText.text = "当前版本 ${versionName}"
            } else {
                binding.versionText.text = ""
            }
        } catch (_: Throwable) {
            binding.versionText.text = ""
        }

        return binding.root
    }

    private fun renderLoginState() {
        if (Session.token.isNullOrEmpty()) {
            binding.nickname.text = "未登录"
            binding.subtitle.text = "点击登录/注册"
        } else {
            binding.nickname.text = "欢迎回来"
            binding.subtitle.text = "已登录，可查看订单"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqLogin && resultCode == Activity.RESULT_OK) {
            renderLoginState()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
