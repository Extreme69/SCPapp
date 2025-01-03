package com.example.scpapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scpapp.api.TalesRepository
import com.example.scpapp.data.tale.Tale
import kotlinx.coroutines.launch

class TalesViewModel(private val repository: TalesRepository = TalesRepository()) : ViewModel() {

    // LiveData for holding all Tales
    val tales = MutableLiveData<List<Tale>>()

    // LiveData for holding search results
    val searchResults = MutableLiveData<List<Tale>>()

    init {
        fetchTales()
    }

    // Function to fetch all Tales
    fun fetchTales() {
        viewModelScope.launch {
            try {
                val data = repository.fetchTales()
                tales.postValue(data ?: emptyList())
            } catch (e: Exception) {
                Log.e("TalesViewModel", "Error fetching Tales", e)
                tales.postValue(emptyList())
            }
        }
    }

    // Function to search SCPs based on query
    fun searchTales(query: String) {
        viewModelScope.launch {
            try {
                val data = repository.searchTales(query) // Call the repository's search function
                searchResults.postValue(data ?: emptyList())
            } catch (e: Exception) {
                Log.e("TalesViewModel", "Error searching Tales", e)
                searchResults.postValue(emptyList())
            }
        }
    }
}