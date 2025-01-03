package com.example.scpapp.data.scp

data class TaleUpdateRequest(
    val title: String? = null,
    val content: String? = null,
    val url: String? = null,
    val scp_id: List<String>? = null
)
