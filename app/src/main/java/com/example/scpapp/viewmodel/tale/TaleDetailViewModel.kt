package com.example.scpapp.viewmodel.tale

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.scpapp.api.TalesRepository
import kotlinx.coroutines.Dispatchers

class TaleDetailViewModel(private val repository: TalesRepository = TalesRepository()) : ViewModel() {
    fun fetchTaleDetails(taleId: String) = liveData(Dispatchers.IO) {
        try {
            val details = repository.getTaleDetails(taleId)
            Log.d("TaleDetailViewModel", "Fetched Tale details: $details")
            emit(details) // Emit the Tale details
        } catch (e: Exception) {
            Log.e("TaleDetailViewModel", "Error fetching Tale details for ID: $taleId", e)
            emit(null) // Emit null in case of error
        }
    }
}