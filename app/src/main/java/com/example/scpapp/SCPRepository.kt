package com.example.scpapp

import android.util.Log

class SCPRepository {
    private val api = RetrofitClient.api

    suspend fun fetchSCPs(): List<SCP>? {
        Log.d("SCPRepository", "Making API request...")
        return try {
            val response = api.getSCPs()
            Log.d("SCPRepository", "Received response: $response")  // Add log to check response object
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Response is successful.")
                val originalData = response.body()
                Log.d("SCPRepository", "Fetched data: $originalData")  // Log the body content
                originalData?.map { scp ->
                    // Format each SCP object as per your app's structure
                    SCP(
                        id = scp.id,
                        title = scp.title,
                        description = scp.description,
                        classification = scp.classification,
                        photoUrl = scp.photoUrl,
                        rating = scp.rating,
                        scpTales = scp.scpTales,
                        url = scp.url,
                        series = scp.series
                    )
                }
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
