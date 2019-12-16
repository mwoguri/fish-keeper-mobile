package com.example.fishkeeper.network

import com.google.gson.annotations.SerializedName

data class Catch(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    @SerializedName("fish_length") val fishLength: Double,
    @SerializedName("fish_species") val fishSpecies: String,
    @SerializedName("fish_weight") val fishWeight: Int,
    @SerializedName("lure_type") val lureType: String,
    @SerializedName("hook_size") val hookSize: Int,
    @SerializedName("timestamp") val timestampString: String,
    val id: Long = 0
) {
    val timestamp: Long
        get() = timestampString.toLong()
}
