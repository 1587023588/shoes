package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoes.databinding.ActivityVillageActivitiesBinding

class VillageActivitiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVillageActivitiesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVillageActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = VillageActivityAdapter(VillageActivitiesRepository.list())
    }
}
