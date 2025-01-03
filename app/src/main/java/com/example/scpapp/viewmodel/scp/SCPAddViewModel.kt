package com.example.scpapp.viewmodel.scp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.SCPRepository
import com.example.scpapp.data.scp.SCPRequest
import kotlinx.coroutines.launch

class SCPAddViewModel(private val repository: SCPRepository = SCPRepository()) : ViewModel() {

    private val _classificationTypes = MutableLiveData<List<String>>()
    val classificationTypes: LiveData<List<String>> = _classificationTypes

    private val _saveSCPResult = MutableLiveData<Result<Unit>>()
    val saveSCPResult: LiveData<Result<Unit>> = _saveSCPResult

    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

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

    fun validateInputs(id: String, title: String, classification: String, url: String, description: String): Boolean {
        return if (classification == "Pick Classification" || id.isBlank() || title.isBlank() || url.isBlank() || description.isBlank()) {
            _validationError.postValue("Please fill in all fields and select a classification.")
            false
        } else {
            _validationError.postValue(null)
            true
        }
    }

    fun saveSCP(id: String, title: String, classification: String, url: String, description: String) {
        viewModelScope.launch {
            try {
                val series = calculateSeries(id)
                val scpRequest = SCPRequest(
                    scp_id = id,
                    title = title,
                    classification = classification,
                    url = url,
                    description = description,
                    series = series
                )

                val response = repository.createSCP(scpRequest)
                if (response.isSuccessful) {
                    _saveSCPResult.postValue(Result.success(Unit))
                } else {
                    _saveSCPResult.postValue(Result.failure(Exception("Failed to save SCP: ${response.code()}")))
                }
            } catch (e: Exception) {
                _saveSCPResult.postValue(Result.failure(e))
            }
        }
    }

    private fun calculateSeries(scpId: String): String {
        val number = scpId.substringAfter("SCP-").toIntOrNull()
        return when (number) {
            in 1..999 -> "I"
            in 1000..1999 -> "II"
            in 2000..2999 -> "III"
            in 3000..3999 -> "IV"
            in 4000..4999 -> "V"
            in 5000..5999 -> "VI"
            in 6000..6999 -> "VII"
            in 7000..7999 -> "VIII"
            in 8000..8999 -> "IX"
            in 9000..9999 -> "X"
            else -> "XI"
        }
    }
}

