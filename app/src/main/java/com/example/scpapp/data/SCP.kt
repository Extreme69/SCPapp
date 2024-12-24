package com.example.scpapp.data
import com.google.gson.annotations.SerializedName

// SCP model class representing the data structure for SCPs
data class SCP(
    @SerializedName("scp_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("classification") val classification: String,
    @SerializedName("photo_url") val photoUrl: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("scp_tales") val scpTales: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("series") val series: String
)
