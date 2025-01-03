package com.example.scpapp.data.scp

data class SCPUpdateRequest(
    val title: String? = null,
    val classification: String? = null,
    val url: String? = null,
    val description: String? = null
)