package com.example.scpapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.TalesRepository
import com.example.scpapp.data.tale.TaleRequest
import kotlinx.coroutines.launch

class TaleAddViewModel(private val repository: TalesRepository = TalesRepository()) : ViewModel() {
    private val _saveTaleResult = MutableLiveData<Result<Unit>>()
    val saveTaleResult: LiveData<Result<Unit>> = _saveTaleResult

    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    fun validateInputs(title: String, url: String, content: String): Boolean {
        return if (title.isBlank() || url.isBlank() || content.isBlank() ) {
            _validationError.postValue("Please fill in all fields and select a classification.")
            false
        } else {
            _validationError.postValue(null)
            true
        }
    }

    fun saveTale(title: String, url: String, content: String, scp_id: List<String>) {
        viewModelScope.launch {
            try {
                val taleRequest = TaleRequest(
                    title = title,
                    url = url,
                    content = content,
                    scp_id = scp_id
                )

                val response = repository.createTale(taleRequest)
                if (response.isSuccessful) {
                    _saveTaleResult.postValue(Result.success(Unit))
                } else {
                    _saveTaleResult.postValue(Result.failure(Exception("Failed to save Tale: ${response.code()}")))
                }
            } catch (e: Exception) {
                _saveTaleResult.postValue(Result.failure(e))
            }
        }
    }
}