package com.example.scpapp.api

import android.util.Log
import com.example.scpapp.data.tale.Tale
import com.example.scpapp.data.tale.TaleRequest
import retrofit2.Response

class TalesRepository {
    private val api = RetrofitClient.api

    suspend fun fetchTales(): List<Tale>? {
        return try {
            val response = api.getTales()
            if (response.isSuccessful) {
                Log.d("TalesRepository", "Response is successful.")
                val originalData = response.body()?.data
                originalData
            } else {
                Log.e("TalesRepository", "Response not successful.")
                null
            }
        } catch (e: Exception) {
            Log.e("TalesRepository", "Error making API request", e)
            e.printStackTrace()
            null
        }
    }

    suspend fun searchTales(query: String): List<Tale>? {
        return try {
            val response = api.searchTales(query)
            if (response.isSuccessful) {
                Log.d("TalesRepository", "Search successful for query: $query")
                val originalData = response.body()?.data
                originalData
            } else {
                Log.e("TalesRepository", "Search failed for query: $query")
                null
            }
        } catch (e: Exception) {
            Log.e("TalesRepository", "Error making search API request", e)
            e.printStackTrace()
            null
        }
    }

    suspend fun getTaleDetails(taleId: String): Tale? {
        return try {
            val response = api.getTaleDetails(taleId)
            if (response.isSuccessful) {
                Log.d("TalesRepository", "Successfully fetched tale details for ID: $taleId")
                response.body()?.data?.firstOrNull()
            } else {
                Log.e("TalesRepository", "Failed to fetch tale details for ID: $taleId")
                null
            }
        } catch (e: Exception) {
            Log.e("TalesRepository", "Error fetching tale details for ID: $taleId", e)
            null
        }
    }

    suspend fun createTale(taleRequest: TaleRequest): Response<Unit> {
        return try {
            val response = api.createTale(taleRequest)
            if (response.isSuccessful) {
                Log.d("TalesRepository", "Successfully created tale: ${taleRequest.title}")
            } else {
                Log.e("TalesRepository", "Failed to create tale: ${response.code()}")
            }
            response
        } catch (e: Exception) {
            Log.e("TalesRepository", "Error creating tale", e)
            throw e
        }
    }
}