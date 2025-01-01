package com.example.scpapp.data
import com.google.gson.annotations.SerializedName

// Tale model class representing the data structure for Tales
data class Tale(
    @SerializedName("tale_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val description: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("scp_id") val scpId: List<String>,
    @SerializedName("url") val url: String,
)
