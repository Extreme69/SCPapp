package com.example.scpapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scpapp.SharedPreferencesRepository
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SharedPreferencesRepository(application)

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _saveSettingsResult = MutableLiveData<Result<Unit>>()
    val saveSettingsResult: LiveData<Result<Unit>> = _saveSettingsResult

    init {
        loadUsername()
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun saveSettings() {
        viewModelScope.launch {
            try {
                val currentUsername = _username.value
                if (currentUsername.isNullOrBlank()) {
                    _saveSettingsResult.postValue(Result.failure(Exception("Username cannot be empty.")))
                    return@launch
                }

                repository.saveUsername(currentUsername)
                _saveSettingsResult.postValue(Result.success(Unit))
            } catch (e: Exception) {
                _saveSettingsResult.postValue(Result.failure(e))
            }
        }
    }

    private fun loadUsername() {
        viewModelScope.launch {
            try {
                _username.value = repository.getUsername()
            } catch (e: Exception) {
                _username.value = "DefaultUser"
            }
        }
    }
}

