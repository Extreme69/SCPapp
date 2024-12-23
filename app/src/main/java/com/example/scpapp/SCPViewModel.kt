package com.example.scpapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

// ViewModel for SCPs
class SCPViewModel(private val repository: SCPRepository = SCPRepository()) : ViewModel() {

    val scps = liveData(Dispatchers.IO) {
        try {
            val data = repository.fetchSCPs()
            Log.d("SCPViewModel", "Fetched SCPs: ${data?.size ?: 0}") // Log the size of the fetched list
            emit(data ?: emptyList()) // Emit data or empty list if null
        } catch (e: Exception) {
            Log.e("SCPViewModel", "Error fetching SCPs", e)
            emit(emptyList<SCP>()) // In case of an error, emit an empty list
        }
    }

    suspend fun fetchSCPs() {
        // This is where you could trigger the fetch manually if needed
        // Usually, liveData will be triggered when you observe it in the activity.
    }
}
