package com.example.scpapp.api

import android.util.Log
import com.example.scpapp.data.SCP

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

    suspend fun getSCPDetails(scpId: String): SCP? {
        return try {
            val response = api.getSCPDetails(scpId)
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Successfully fetched SCP details for ID: $scpId")
                response.body()?.data?.data?.firstOrNull() // Adjust based on your response structure
            } else {
                Log.e("SCPRepository", "Failed to fetch SCP details for ID: $scpId")
                null
            }
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error fetching SCP details for ID: $scpId", e)
            null
        }
    }
}
