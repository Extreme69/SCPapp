package com.example.scpapp.data.scp

data class SCPResponse(
    val data: SCPResponseData
)

data class SCPResponseData(
    val totalPages: Int,
    val currentPage: Int,
    val data: List<SCP>
)
