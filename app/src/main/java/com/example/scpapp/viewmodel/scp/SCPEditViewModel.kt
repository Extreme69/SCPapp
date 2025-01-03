package com.example.scpapp.viewmodel.scp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.SCPRepository
import com.example.scpapp.data.scp.SCPUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SCPEditViewModel(private val repository: SCPRepository = SCPRepository()) : ViewModel() {
    private val _classificationTypes = MutableLiveData<List<String>>()
    val classificationTypes: LiveData<List<String>> = _classificationTypes

    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    private val _saveSCPResult = MutableLiveData<Result<Unit>>()
    val saveSCPResult: LiveData<Result<Unit>> = _saveSCPResult

    private val _deleteSCPResult = MutableLiveData<Result<Unit>>()
    val deleteSCPResult: LiveData<Result<Unit>> = _deleteSCPResult

    init {
        loadClassificationTypes()
    }

    private fun loadClassificationTypes() {
        val classifications = listOf(
            "Pick Classification", "Safe", "Euclid", "Keter", "Thaumiel", "Apollyon",
            "Archon", "Ticonderoga", "Explained", "Neutralized", "Decommissioned",
            "Pending", "Uncontained"
        )
        _classificationTypes.postValue(classifications)
    }

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

    fun validateInputs(title: String, classification: String, url: String, description: String): Boolean {
        return if (classification == "Pick Classification" || title.isBlank() || url.isBlank() || description.isBlank()) {
            _validationError.postValue("Please fill in all fields and select a classification.")
            false
        } else {
            _validationError.postValue(null)
            true
        }
    }

    fun saveSCP(id: String, updatedFields: SCPUpdateRequest) {
        viewModelScope.launch {
            try {
                // Make sure the updatedFields are not empty (nulls will be excluded in the request body)
                if (updatedFields != SCPUpdateRequest()) {  // Check if there's any change
                    val response = repository.updateSCP(id, updatedFields)
                    if (response.isSuccessful) {
                        _saveSCPResult.postValue(Result.success(Unit))
                    } else {
                        _saveSCPResult.postValue(Result.failure(Exception("Failed to save SCP: ${response.code()}")))
                    }
                } else {
                    // If no fields are updated, do not make the request
                    _saveSCPResult.postValue(Result.failure(Exception("No changes detected.")))
                }
            } catch (e: Exception) {
                _saveSCPResult.postValue(Result.failure(e))
            }
        }
    }

    fun deleteSCP(scpId: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteSCP(scpId)
                if (response.isSuccessful) {
                    _deleteSCPResult.postValue(Result.success(Unit))
                } else {
                    _deleteSCPResult.postValue(Result.failure(Exception("Failed to delete SCP: ${response.code()}")))
                }
            } catch (e: Exception) {
                _deleteSCPResult.postValue(Result.failure(e))
            }
        }
    }
}