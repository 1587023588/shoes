package com.example.shoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoes.databinding.ActivityPosterBinding

class PosterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPosterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resId = intent.getIntExtra(EXTRA_RES_ID, 0)
        if (resId != 0) {
            binding.posterImage.setImageResource(resId)
        }
        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_RES_ID = "res_id"
    }
}
