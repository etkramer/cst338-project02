package com.etkramer.project02

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.etkramer.project02.adapters.CartItemAdapter
import com.etkramer.project02.adapters.LandingItemAdapter
import com.etkramer.project02.databinding.ActivityCartBinding
import com.etkramer.project02.databinding.ActivityLoginBinding
import com.etkramer.project02.db.AppDatabase

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)

        val currentUser = db.getCurrentUserOrNull(this) ?: throw Exception()
        val cart = db.userDao().getCart(currentUser.id)

        binding.cartRecycler.adapter = CartItemAdapter(this, cart)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.checkoutButton.setOnClickListener {
            // TODO: Impl
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CartActivity::class.java)
        }
    }
}
