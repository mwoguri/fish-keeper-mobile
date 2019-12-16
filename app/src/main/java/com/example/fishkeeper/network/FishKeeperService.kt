package com.example.fishkeeper.network

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://temp.com"

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(FakeInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    //.addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface FishKeeperService {
    @GET("catch")
    fun listCatches(): Call<List<Catch>>
}

object FishKeeperApi {
    val retrofitService: FishKeeperService by lazy {
        retrofit.create(FishKeeperService::class.java)
    }
}