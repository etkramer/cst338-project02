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
import com.etkramer.project02.db.Product

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
            startActivity(MainActivity.getIntent(this))
        }

        binding.checkoutButton.setOnClickListener {
            val items = cart.value as List<Product>
            val totalCost = items.sumOf { item -> item.price }

            // Make sure cart is affordable
            if (totalCost > currentUser.balance) {
                Toast.makeText(this,
                    "Can't afford items! Subtotal is $${totalCost} but your balance is only $${currentUser.balance}",
                    Toast.LENGTH_LONG).show()

                return@setOnClickListener
            }

            // Update balance
            db.userDao().insert(currentUser.copy(balance = currentUser.balance - totalCost))

            for (item in items)
            {
                val edge = db.userProductEdgeDao().findWithIds(currentUser.id, item.id)
                    ?.copy(isInCart = false) ?: throw Exception()

                db.userProductEdgeDao().insert(edge)
            }

            Toast.makeText(this, "Checked out ${items.size} items for $${totalCost}", Toast.LENGTH_LONG).show();

            finish()
            startActivity(LandingActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CartActivity::class.java)
        }
    }
}
