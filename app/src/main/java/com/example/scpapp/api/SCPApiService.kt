package com.example.scpapp.api

import com.example.scpapp.data.SCPResponse
import retrofit2.Response
import retrofit2.http.GET

interface SCPApiService {
    @GET("SCPs")
    suspend fun getSCPs(): Response<SCPResponse>
}
