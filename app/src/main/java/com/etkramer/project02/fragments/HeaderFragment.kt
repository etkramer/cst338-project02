package com.etkramer.project02.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.etkramer.project02.CartActivity
import com.etkramer.project02.MainActivity
import com.etkramer.project02.Prefs
import com.etkramer.project02.R
import com.etkramer.project02.USERNAME_KEY
import com.etkramer.project02.db.AppDatabase
import com.etkramer.project02.db.User

class HeaderFragment : Fragment(R.layout.header_fragment) {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameDisplay = view.findViewById(R.id.username_display) as TextView
        val balanceDisplay = view.findViewById(R.id.balance_display) as TextView

        val logoutButton = view.findViewById(R.id.logout_button) as Button
        val deleteAccountButton = view.findViewById(R.id.delete_account_button) as Button
        val renameAccountButton = view.findViewById(R.id.rename_account_button) as Button
        val cartButton = view.findViewById(R.id.cart_button) as Button

        val context = activity as Context

        val db = AppDatabase.getInstance(context)

        val username = Prefs.getPrefs(context).getString(USERNAME_KEY, null)
        val user = db.userDao().findByName(username as String)

        if (user != null) {
            usernameDisplay.text =
                "Logged in: ${user.username} (${if(user.isAdmin) "admin" else "not admin"})"

            balanceDisplay.text = "Balance: $${user.balance}"
        }

        logoutButton.setOnClickListener {
            logoutUser(context)
        }

        deleteAccountButton.setOnClickListener {
            db.userDao().delete(db.getCurrentUserOrNull(context) as User)
            logoutUser(context)
        }

        renameAccountButton.setOnClickListener {
            val textInput = EditText(context).apply {
                inputType = InputType.TYPE_CLASS_TEXT
            }

            AlertDialog.Builder(context)
                .setTitle("Rename account")
                .setView(textInput)
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
                .setPositiveButton("Rename and log out") { _, _ ->
                    val newUser = db.getCurrentUserOrNull(context)?.copy(username = textInput.text.toString())
                    if (newUser != null) {
                        db.userDao().insert(newUser)
                        Toast.makeText(context, "Renamed to ${newUser.username}", Toast.LENGTH_SHORT).show()

                        logoutUser(context)
                    }
                }
                .show()
        }

        cartButton.setOnClickListener {
            startActivity(CartActivity.getIntent(context))
        }
    }

    private fun logoutUser(context: Context) {
        // Clear username
        with (Prefs.getPrefs(context).edit()) {
            putString("USERNAME_KEY", null)
            apply()
        }

        startActivity(MainActivity.getIntent(context))
    }
}
