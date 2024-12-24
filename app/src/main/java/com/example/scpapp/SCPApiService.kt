package com.example.scpapp

import retrofit2.Response
import retrofit2.http.GET

interface SCPApiService {
    @GET("SCPs")
    suspend fun getSCPs(): Response<SCPResponse>
}
