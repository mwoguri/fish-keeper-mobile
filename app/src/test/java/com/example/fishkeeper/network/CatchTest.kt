package com.example.fishkeeper.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Test

class CatchTest {

    private val gson = Gson()

    private val expectedCatchPost = CatchPost(
        77.754500,
        -101.413500,
        4800.00,
        10.00,
        "Brook trout",
        8,
        "Pheasant Tail",
        24,
        1415617165516
    )

    private val expectedCatchResponse1 = CatchResponse(
        77.754500,
        -101.413500,
        4800.00,
        10.00,
        "Brook trout",
        8,
        "Pheasant Tail",
        24,
        "1415617165516",
        123
    )

    private val expectedCatchResponse2 = CatchResponse(
        62.998000,
        -99.637600,
        9867.00,
        14.00,
        "Rainbow trout",
        22,
        "Elk Hair Caddis",
        18,
        "1575514483506",
        99
    )

    @Test
    fun catchPostParses() {

        val jsonCatch = """{ 
                "latitude": 77.754500, 
                "longitude": -101.413500, 
                "altitude": 4800.00, 
                "fish_length": 10.00, 
                "fish_species": "Brook trout",
                "fish_weight": 8,
                "lure_type": "Pheasant Tail", 
                "hook_size": 24, 
                "timestamp": 1415617165516
                }"""
        val catch = gson.fromJson(jsonCatch, CatchPost::class.java)
        assertEquals(expectedCatchPost, catch)
    }


    @Test
    fun catchesParses() {

        val jsonCatches = """
             [
              {
                "id": 123,
                "latitude": 77.754500, 
                "longitude": -101.413500, 
                "altitude": 4800.00, 
                "fish_length": 10.00, 
                "fish_species": "Brook trout",
                "fish_weight": 8,
                "lure_type": "Pheasant Tail", 
                "hook_size": 24, 
                "timestamp": "1415617165516"
              },
              {
                "id": 99,
                "latitude": 62.998000,
                "longitude": -99.637600,
                "altitude": 9867.00,
                "fish_length": 14.00,
                "fish_species": "Rainbow trout",
                "fish_weight": 22,
                "lure_type": "Elk Hair Caddis",
                "hook_size": 18,
                "timestamp": "1575514483506"
              }
            ]
        """
        val type = object : TypeToken<List<CatchResponse>>() {}.type

        val catchResponses: List<CatchResponse> = gson.fromJson(jsonCatches, type)
        val expectedCatchResponses: List<CatchResponse> =
            listOf(expectedCatchResponse1, expectedCatchResponse2)

        assertEquals(expectedCatchResponses, catchResponses)

    }
}
