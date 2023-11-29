package com.etkramer.project02

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.etkramer.project02.databinding.ActivityMainBinding
import com.etkramer.project02.db.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)
        if (db.getCurrentUserOrNull(this) != null) {
            // Already logged in
            startActivity(LandingActivity.getIntent(this))
        } else {
            // Clear any saved username just in case
            with (Prefs.getPrefs(this).edit()) {
                putString("USERNAME_KEY", null)
                apply()
            }
        }

        binding.loginButton.setOnClickListener {
            startActivity(LoginActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
