package com.example.fishkeeper.network

import com.google.gson.annotations.SerializedName

data class CatchPost(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    @SerializedName("fish_length") val fishLength: Double,
    @SerializedName("fish_species") val fishSpecies: String,
    @SerializedName("fish_weight") val fishWeight: Int,
    @SerializedName("lure_type") val lureType: String,
    @SerializedName("hook_size") val hookSize: Int,
    val timestamp: Long
)
