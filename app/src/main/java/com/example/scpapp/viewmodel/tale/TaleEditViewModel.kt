package com.example.scpapp.viewmodel.tale

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.TalesRepository
import com.example.scpapp.data.scp.TaleUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaleEditViewModel(private val repository: TalesRepository = TalesRepository()) : ViewModel() {
    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    private val _saveTaleResult = MutableLiveData<Result<Unit>>()
    val saveTaleResult: LiveData<Result<Unit>> = _saveTaleResult

    private val _deleteTaleResult = MutableLiveData<Result<Unit>>()
    val deleteTaleResult: LiveData<Result<Unit>> = _deleteTaleResult

    fun fetchTaleDetails(taleId: String) = liveData(Dispatchers.IO) {
        try {
            val details = repository.getTaleDetails(taleId)
            Log.d("SCPDetailViewModel", "Fetched tale details: $details")
            emit(details)
        } catch (e: Exception) {
            Log.e("SCPDetailViewModel", "Error fetching tale details for ID: $taleId", e)
            emit(null)
        }
    }

    fun validateInputs(title: String, url: String, content: String): Boolean {
        return if (title.isBlank() || url.isBlank() || content.isBlank()) {
            _validationError.postValue("Please fill in all fields and select a classification.")
            false
        } else {
            _validationError.postValue(null)
            true
        }
    }

    fun saveSCP(id: String, updatedFields: TaleUpdateRequest) {
        viewModelScope.launch {
            try {
                // Make sure the updatedFields are not empty (nulls will be excluded in the request body)
                if (updatedFields != TaleUpdateRequest()) {  // Check if there's any change
                    val response = repository.updateTale(id, updatedFields)
                    if (response.isSuccessful) {
                        _saveTaleResult.postValue(Result.success(Unit))
                    } else {
                        _saveTaleResult.postValue(Result.failure(Exception("Failed to save tale: ${response.code()}")))
                    }
                } else {
                    // If no fields are updated, do not make the request
                    _saveTaleResult.postValue(Result.failure(Exception("No changes detected.")))
                }
            } catch (e: Exception) {
                _saveTaleResult.postValue(Result.failure(e))
            }
        }
    }

    fun deleteTale(taleId: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteTale(taleId)
                if (response.isSuccessful) {
                    _deleteTaleResult.postValue(Result.success(Unit))
                } else {
                    _deleteTaleResult.postValue(Result.failure(Exception("Failed to delete tale: ${response.code()}")))
                }
            } catch (e: Exception) {
                _deleteTaleResult.postValue(Result.failure(e))
            }
        }
    }
}