package com.example.scpapp.viewmodel.scp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.scpapp.api.SCPRepository
import kotlinx.coroutines.Dispatchers

class SCPDetailViewModel(private val repository: SCPRepository = SCPRepository()) : ViewModel() {

    fun fetchSCPDetails(scpId: String) = liveData(Dispatchers.IO) {
        try {
            val details = repository.getSCPDetails(scpId)
            Log.d("SCPDetailViewModel", "Fetched SCP details: $details")
            emit(details) // Emit the SCP details
        } catch (e: Exception) {
            Log.e("SCPDetailViewModel", "Error fetching SCP details for ID: $scpId", e)
            emit(null) // Emit null in case of error
        }
    }

    fun fetchTaleDetails(taleId: String) = liveData(Dispatchers.IO) {
        try {
            val taleDetails = repository.getTaleDetails(taleId)
            Log.d("SCPDetailViewModel", "Fetched Tale details: $taleDetails")
            emit(taleDetails)
        } catch (e: Exception) {
            Log.e("SCPDetailViewModel", "Error fetching Tale details for ID: $taleId", e)
            emit(null)
        }
    }
}
