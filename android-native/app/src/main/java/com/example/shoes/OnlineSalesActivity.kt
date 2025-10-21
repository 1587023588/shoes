package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoes.data.ProductRepository
import com.example.shoes.databinding.ActivityOnlineSalesBinding
import com.example.shoes.ui.home.ProductAdapter

class OnlineSalesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnlineSalesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnlineSalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val products = ProductRepository.list()
        binding.list.layoutManager = GridLayoutManager(this, 2)
        binding.list.adapter = ProductAdapter(products) { product ->
            startActivity(ProductDetailActivity.intent(this, product.id))
        }
    }
}
