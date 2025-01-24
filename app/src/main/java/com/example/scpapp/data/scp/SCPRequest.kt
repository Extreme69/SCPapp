package com.example.scpapp.data.scp

data class SCPRequest(
    val scp_id: String,
    val title: String,
    val description: String,
    val classification: String,
    val photo_url: String? = null,
    val rating: Int = 0,                        // When creating an SCP it's always 0
    val scp_tales: List<String> = emptyList(),  // This is always empty, the tales get added automatically
    val url: String,
    val series: String,
    val creator: String
)
