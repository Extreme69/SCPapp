package com.example.scpapp.data.tale

data class TaleResponse(
    val totalPages: Int,
    val currentPage: Int,
    val data: List<Tale>
)
