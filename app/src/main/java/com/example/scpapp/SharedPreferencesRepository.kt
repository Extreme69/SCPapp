package com.example.scpapp

import android.content.Context

class SharedPreferencesRepository(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun getUsername(): String {
        return sharedPreferences.getString("username", "DefaultUser") ?: "DefaultUser"
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
    }
}
