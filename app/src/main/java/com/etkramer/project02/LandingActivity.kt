package com.etkramer.project02

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.etkramer.project02.adapters.LandingItemAdapter
import com.etkramer.project02.databinding.ActivityLandingBinding
import com.etkramer.project02.db.AppDatabase
import com.etkramer.project02.db.User

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)

        val username = Prefs.getPrefs(this).getString(USERNAME_KEY, null)
        val user = db.userDao().findByName(username as String)

        if (user != null) {
            binding.usernameDisplay.text =
                "Logged in: ${user.username} (${if(user.isAdmin) "admin" else "not admin"})"

            binding.balanceDisplay.text = "Balance: $${user.balance}"
        }

        binding.logoutButton.setOnClickListener {
            logoutUser()
        }

        binding.deleteAccountButton.setOnClickListener {
            db.userDao().delete(db.getCurrentUserOrNull(this) as User)
            logoutUser()
        }

        binding.renameAccountButton.setOnClickListener {
            val textInput = EditText(this).apply {
                inputType = InputType.TYPE_CLASS_TEXT
            }

            AlertDialog.Builder(this)
                .setTitle("Rename account")
                .setView(textInput)
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
                .setPositiveButton("Rename and log out") { _, _ ->
                    val newUser = db.getCurrentUserOrNull(this)?.copy(username = textInput.text.toString())
                    if (newUser != null) {
                        db.userDao().insert(newUser)
                        Toast.makeText(this, "Renamed to ${newUser.username}", Toast.LENGTH_SHORT).show()

                        logoutUser()
                    }
                }
                .show()
        }

        binding.cartButton.setOnClickListener {
            startActivity(CartActivity.getIntent(this))
        }

        val products = db.productDao().getAll()
        binding.productRecycler.adapter = LandingItemAdapter(this, products)
    }

    private fun logoutUser() {
        // Clear username
        with (Prefs.getPrefs(this).edit()) {
            putString("USERNAME_KEY", null)
            apply()
        }

        startActivity(MainActivity.getIntent(this))
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LandingActivity::class.java)
        }
    }
}
