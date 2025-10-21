package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.databinding.ActivityXibaopoBinding

class XibaopoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityXibaopoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXibaopoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
