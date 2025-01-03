package com.example.scpapp.data.tale
import com.google.gson.annotations.SerializedName

// Tale model class representing the data structure for Tales
data class Tale(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("scp_id") val scpId: List<String>,
    @SerializedName("url") val url: String,
)
