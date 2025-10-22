package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoes.databinding.ActivityCouponBinding

class CouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCouponBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = CouponAdapter(CouponRepository.list())
    }
}
