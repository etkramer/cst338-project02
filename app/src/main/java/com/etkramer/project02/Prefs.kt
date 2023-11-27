package com.etkramer.project02

import android.content.Context
import android.content.SharedPreferences

const val USERNAME_KEY = "USERNAME_KEY"
const val PREFS_KEY = "PREFS_KEY"

class Prefs {
    companion object {
        fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        }
    }
}
