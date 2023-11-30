package com.etkramer.project02

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.etkramer.project02.databinding.ActivityLoginBinding
import com.etkramer.project02.db.AppDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)

        binding.submitButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()

            val user = db.userDao().findByName(username)
            if (user == null || user.password != password) {
                Toast.makeText(this, "Username or password was incorrect!", Toast.LENGTH_LONG)
                    .show()

                return@setOnClickListener
            }

            Toast.makeText(this, "Login successful!", Toast.LENGTH_LONG)
                .show()

            // Store username
            with (Prefs.getPrefs(this).edit()) {
                putString("USERNAME_KEY", username)
                apply()
            }

            startActivity(LandingActivity.getIntent(this))
        }

        binding.autofillButton.setOnClickListener {
            binding.usernameInput.setText("admin2");
            binding.passwordInput.setText("admin2");
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
