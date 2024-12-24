package com.example.scpapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.41:3000/"

    // Create a logging interceptor to capture HTTP requests and responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log full request and response details
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add the interceptor to the client
        .build()

    val api: SCPApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Add the client with the logging interceptor
            .addConverterFactory(GsonConverterFactory.create()) // For parsing JSON
            .build()
            .create(SCPApiService::class.java)
    }
}
