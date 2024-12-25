package com.example.scpapp.api

import com.example.scpapp.data.SCPResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SCPApiService {
    @GET("SCPs")
    suspend fun getSCPs(): Response<SCPResponse>

    @GET("SCPs")
    suspend fun getSCPDetails(@Query("scp_id") scpId: String): Response<SCPResponse>
}
