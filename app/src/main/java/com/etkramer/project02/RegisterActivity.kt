package com.etkramer.project02

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.etkramer.project02.databinding.ActivityLoginBinding
import com.etkramer.project02.databinding.ActivityRegisterBinding
import com.etkramer.project02.db.AppDatabase
import com.etkramer.project02.db.User

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)

        binding.submitButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()

            // Make sure inputs are valid
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Must enter a valid (non-empty) username and password!", Toast.LENGTH_LONG)
                    .show()

                return@setOnClickListener
            }

            // Make sure username isn't in use
            val user = db.userDao().findByName(username)
            if (user != null) {
                Toast.makeText(this, "Username is already in use!", Toast.LENGTH_LONG)
                    .show()

                binding.usernameInput.setText("")
                binding.passwordInput.setText("")

                return@setOnClickListener
            }

            // Add to db
            db.userDao().insert(User(0, false, username, password, 50))

            Toast.makeText(this, "Created new account with $50 starting balance!", Toast.LENGTH_LONG)
                .show()

            // Return to main menu
            startActivity(MainActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}
