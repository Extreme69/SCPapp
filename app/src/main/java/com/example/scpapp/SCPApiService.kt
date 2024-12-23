package com.example.scpapp

import retrofit2.Response
import retrofit2.http.GET

// Define the API service interface for Retrofit
interface SCPApiService {

    @GET("SCPs")
    suspend fun getSCPs(): Response<List<SCP>>
}
