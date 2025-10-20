package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.databinding.ActivityBookingBinding
import android.app.DatePickerDialog
import java.util.Calendar

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 固定时段展示
        binding.tvTime.text = "09:00 - 17:00（固定）"

        // 日期选择
        binding.tvDate.setOnClickListener {
            val c = Calendar.getInstance()
            val dlg = DatePickerDialog(this, { _, y, m, d ->
                val mm = (m + 1).toString().padStart(2, '0')
                val dd = d.toString().padStart(2, '0')
                binding.tvDate.text = "$y-$mm-$dd"
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
            dlg.datePicker.minDate = System.currentTimeMillis() // 不能约今天之前
            dlg.show()
        }

        // 提交
        binding.btnSubmit.setOnClickListener {
            val date = binding.tvDate.text?.toString()?.trim().orEmpty()
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
            if (date.isEmpty() || date.contains("点击选择")) {
                binding.tvResult.text = "请先选择日期"
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                binding.tvResult.text = "请输入联系人姓名"
                return@setOnClickListener
            }
            if (!phone.matches(Regex("^1[3-9]\\d{9}$"))) {
                binding.tvResult.text = "请输入有效的手机号"
                return@setOnClickListener
            }
            binding.tvResult.text = "已提交预约：$date 09:00-17:00，联系人：$name，电话：$phone"
        }
    }
}
