package com.example.scpapp.data.tale

data class TaleRequest(
    val title: String,
    val content: String,
    val rating: Int = 0,                        // When creating a tale it's always 0
    val scp_id: List<String>,
    val url: String,
)
