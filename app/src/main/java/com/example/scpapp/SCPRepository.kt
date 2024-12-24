package com.example.scpapp

import android.util.Log

class SCPRepository {
    private val api = RetrofitClient.api

    suspend fun fetchSCPs(): List<SCP>? {
        return try {
            val response = api.getSCPs()
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Response is successful.")
                val originalData = response.body()?.data?.data // Access nested 'data'
                originalData
            } else {
                Log.e("SCPRepository", "Response not successful.")
                null
            }
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error making API request", e)
            e.printStackTrace()
            null
        }
    }
}
