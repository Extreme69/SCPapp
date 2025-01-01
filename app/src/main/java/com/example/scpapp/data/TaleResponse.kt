package com.example.scpapp.data

data class TaleResponse(
    val totalPages: Int,
    val currentPage: Int,
    val data: List<Tale>
)
