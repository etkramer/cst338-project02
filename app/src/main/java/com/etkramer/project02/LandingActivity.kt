package com.etkramer.project02

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val username = Prefs.getPrefs(this).getString(USERNAME_KEY, null)
        val user = db.userDao().userByName(username as String)

        if (user != null) {
            binding.usernameDisplay.text =
                "Logged in: ${user.username} (${if(user.isAdmin) "admin" else "not admin"})"
        }

        binding.logoutButton.setOnClickListener {
            // Clear username
            with (Prefs.getPrefs(this).edit()) {
                putString("USERNAME_KEY", null)
                apply()
            }

            startActivity(MainActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LandingActivity::class.java)
        }
    }
}
