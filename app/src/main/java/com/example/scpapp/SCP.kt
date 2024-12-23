package com.example.scpapp

// SCP model class representing the data structure for SCPs
data class SCP(
    val scpId: String,
    val title: String,
    val description: String,
    val classification: String,
    val photoUrl: String,
    val rating: Int,
    val scpTales: Array<String>,
    val url: String,
    val series: String
)
