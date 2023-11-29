package com.etkramer.project02

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.etkramer.project02.adapters.LandingItemAdapter
import com.etkramer.project02.databinding.ActivityLandingBinding
import com.etkramer.project02.db.AppDatabase

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)

        val products = db.productDao().getAll()
        binding.productRecycler.adapter = LandingItemAdapter(this, products)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LandingActivity::class.java)
        }
    }
}
