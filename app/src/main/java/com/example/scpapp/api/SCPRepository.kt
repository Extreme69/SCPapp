package com.example.scpapp.api

import android.util.Log
import com.example.scpapp.data.scp.SCP
import com.example.scpapp.data.scp.SCPRequest
import com.example.scpapp.data.scp.SCPUpdateRequest
import com.example.scpapp.data.tale.Tale
import retrofit2.Response

class SCPRepository {
    private val api = RetrofitClient.api

    suspend fun fetchSCPs(): List<SCP>? {
        return try {
            val response = api.getSCPs()
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Response is successful.")
                val originalData = response.body()?.data?.data
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
                response.body()?.data?.data?.firstOrNull()
            } else {
                Log.e("SCPRepository", "Failed to fetch SCP details for ID: $scpId")
                null
            }
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error fetching SCP details for ID: $scpId", e)
            null
        }
    }

    suspend fun createSCP(scpRequest: SCPRequest): Response<Unit> {
        return try {
            val response = api.createSCP(scpRequest)
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Successfully created SCP: ${scpRequest.scp_id}")
            } else {
                Log.e("SCPRepository", "Failed to create SCP: ${response.code()}")
            }
            response
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error creating SCP", e)
            throw e
        }
    }

    suspend fun updateSCP(id: String, updatedFields: SCPUpdateRequest): Response<Unit> {
        return try {
            val response = api.updateSCP(id, updatedFields)
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Successfully updated SCP: $id")
            } else {
                Log.e("SCPRepository", "Failed to update SCP: ${response.code()}")
            }
            response
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error updating SCP", e)
            throw e
        }
    }

    suspend fun deleteSCP(scpId: String): Response<Unit> {
        return try {
            val response = api.deleteSCP(scpId)
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Successfully deleted SCP: $scpId")
            } else {
                Log.e("SCPRepository", "Failed to delete SCP: ${response.code()}")
            }
            response
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error deleting SCP", e)
            throw e
        }
    }

    suspend fun searchSCPs(query: String): List<SCP>? {
        return try {
            val response = api.searchSCPs(query)
            if (response.isSuccessful) {
                Log.d("SCPRepository", "Search successful for query: $query")
                val originalData = response.body()?.data?.data
                originalData
            } else {
                Log.e("SCPRepository", "Search failed for query: $query")
                null
            }
        } catch (e: Exception) {
            Log.e("SCPRepository", "Error making search API request", e)
            e.printStackTrace()
            null
        }
    }

    // Only used for getting the title of related tales.
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
}
