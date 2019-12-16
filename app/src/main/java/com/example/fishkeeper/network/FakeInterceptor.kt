package com.example.fishkeeper.network

import android.util.Log
import okhttp3.*


class FakeInterceptor : Interceptor {
    private val responseString = """
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

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("Interceptor", "intercept!")
        return Response.Builder()
            .code(200)
            .message(responseString)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.toByteArray()
                )
            )
            .addHeader("content-type", "application/json")
            .build()
    }

}
