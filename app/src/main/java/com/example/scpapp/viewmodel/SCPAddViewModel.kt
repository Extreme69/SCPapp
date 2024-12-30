package com.example.scpapp.viewmodel

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.SCPRepository
import com.example.scpapp.data.SCPRequest
import kotlinx.coroutines.launch

class SCPAddViewModel(private val repository: SCPRepository = SCPRepository()) : ViewModel() {

    // LiveData for classification options
    private val _classificationTypes = MutableLiveData<List<String>>()
    val classificationTypes: LiveData<List<String>> = _classificationTypes

    // LiveData for save SCP result
    private val _saveSCPResult = MutableLiveData<Result<Unit>>()
    val saveSCPResult: LiveData<Result<Unit>> = _saveSCPResult

    // LiveData for validation errors
    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    init {
        loadClassificationTypes()
    }

    // Load classification options
    private fun loadClassificationTypes() {
        val classifications = listOf(
            "Pick Classification", // Default hint text
            "Safe", "Euclid", "Keter", "Thaumiel", "Apollyon", "Archon",
            "Ticonderoga", "Explained", "Neutralized", "Decommissioned", "Pending", "Uncontained"
        )
        _classificationTypes.postValue(classifications)
    }

    // Adapter for the classification spinner
    fun getClassificationAdapter(context: Context): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, _classificationTypes.value ?: listOf()) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).apply {
                    textSize = 24f
                    setTextColor(
                        if (position == 0) ContextCompat.getColor(context, android.R.color.darker_gray)
                        else ContextCompat.getColor(context, android.R.color.white)
                    )
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).apply {
                    textSize = 24f
                    setTextColor(
                        if (position == 0) ContextCompat.getColor(context, android.R.color.darker_gray)
                        else ContextCompat.getColor(context, android.R.color.white)
                    )
                    setBackgroundColor(
                        if (position == 0) ContextCompat.getColor(context, android.R.color.white)
                        else ContextCompat.getColor(context, android.R.color.darker_gray)
                    )
                }
                return view
            }
        }
    }

    // Validate inputs
    fun validateInputs(id: String, title: String, classification: String, url: String, description: String): Boolean {
        return if (classification == "Pick Classification" || id.isBlank() || title.isBlank() || url.isBlank() || description.isBlank()) {
            _validationError.postValue("Please fill in all fields and select a classification.")
            false
        } else {
            _validationError.postValue(null)
            true
        }
    }

    // Save SCP to the backend
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
                    series = series // Assuming the backend supports a 'series' field
                )

                // Use the repository to create SCP
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

    // Calculate SCP series based on ID
    private fun calculateSeries(scpId: String): String {
        val number = scpId.substringAfter("SCP-").toIntOrNull()
        return when {
            number == null -> throw IllegalArgumentException("Invalid SCP ID format")
            number in 1..999 -> "I"
            number in 1000..1999 -> "II"
            number in 2000..2999 -> "III"
            number in 3000..3999 -> "IV"
            number in 4000..4999 -> "V"
            number in 5000..5999 -> "VI"
            number in 6000..6999 -> "VII"
            number in 7000..7999 -> "VIII"
            number in 8000..8999 -> "IX"
            number in 9000..9999 -> "X"
            else -> "XI"
        }
    }
}
