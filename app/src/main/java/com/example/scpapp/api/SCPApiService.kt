package com.example.scpapp.api

import com.example.scpapp.data.SCPRequest
import com.example.scpapp.data.SCPResponse
import com.example.scpapp.data.SCPUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SCPApiService {
    @GET("SCPs")
    suspend fun getSCPs(): Response<SCPResponse>

    @GET("SCPs")
    suspend fun getSCPDetails(@Query("scp_id") scpId: String): Response<SCPResponse>

    @GET("SCPs")
    suspend fun searchSCPs(@Query("search") search: String): Response<SCPResponse>

    @POST("SCPs")
    suspend fun createSCP(@Body scp: SCPRequest): Response<Unit>

    @PUT("SCPs/{id}")
    suspend fun updateSCP(
        @Path("id") scpId: String,
        @Body updatedFields: SCPUpdateRequest
    ): Response<Unit>

    @DELETE("SCPs/{id}")
    suspend fun deleteSCP(@Path("id") scpId: String): Response<Unit>
}
