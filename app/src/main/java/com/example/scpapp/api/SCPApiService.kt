package com.example.scpapp.api

import com.example.scpapp.data.scp.SCPRequest
import com.example.scpapp.data.scp.SCPResponse
import com.example.scpapp.data.scp.SCPUpdateRequest
import com.example.scpapp.data.tale.TaleRequest
import com.example.scpapp.data.tale.TaleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SCPApiService {
    // Get all SCPs
    @GET("SCPs")
    suspend fun getSCPs(): Response<SCPResponse>

    // Get SCP details by id
    @GET("SCPs")
    suspend fun getSCPDetails(@Query("scp_id") scpId: String): Response<SCPResponse>

    // Search SCPs
    @GET("SCPs")
    suspend fun searchSCPs(@Query("search") search: String): Response<SCPResponse>

    // Create an SCP
    @POST("SCPs")
    suspend fun createSCP(@Body scp: SCPRequest): Response<Unit>

    // Update an SCP
    @PUT("SCPs/{id}")
    suspend fun updateSCP(
        @Path("id") scpId: String,
        @Body updatedFields: SCPUpdateRequest
    ): Response<Unit>

    // Delete an SCP
    @DELETE("SCPs/{id}")
    suspend fun deleteSCP(@Path("id") scpId: String): Response<Unit>

    // Get all tales
    @GET("SCPTales")
    suspend fun getTales(): Response<TaleResponse>

    // Search tales
    @GET("SCPTales")
    suspend fun searchTales(@Query("search") search: String): Response<TaleResponse>

    // Get tale details by id
    @GET("SCPTales")
    suspend fun getTaleDetails(@Query("_id") taleId: String): Response<TaleResponse>

    // Create a tale
    @POST("SCPTales")
    suspend fun createTale(@Body tale: TaleRequest): Response<Unit>
}
