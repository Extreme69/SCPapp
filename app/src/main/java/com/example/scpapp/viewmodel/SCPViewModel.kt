package com.example.scpapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.SCPRepository
import com.example.scpapp.data.SCP
import kotlinx.coroutines.launch

// ViewModel for SCPs
class SCPViewModel(private val repository: SCPRepository = SCPRepository()) : ViewModel() {

    // LiveData for holding all SCPs
    val scps = MutableLiveData<List<SCP>>()

    // LiveData for holding search results
    val searchResults = MutableLiveData<List<SCP>>()

    init {
        // Fetch SCPs initially
        fetchSCPs()
    }

    // Function to fetch all SCPs
    fun fetchSCPs() {
        viewModelScope.launch {
            try {
                val data = repository.fetchSCPs()
                scps.postValue(data ?: emptyList())
            } catch (e: Exception) {
                Log.e("SCPViewModel", "Error fetching SCPs", e)
                scps.postValue(emptyList())
            }
        }
    }

    // Function to search SCPs based on query
    fun searchSCPs(query: String) {
        viewModelScope.launch {
            try {
                val data = repository.searchSCPs(query) // Call the repository's search function
                searchResults.postValue(data ?: emptyList())
            } catch (e: Exception) {
                Log.e("SCPViewModel", "Error searching SCPs", e)
                searchResults.postValue(emptyList())
            }
        }
    }
}
