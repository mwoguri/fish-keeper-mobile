package com.example.fishkeeper.network

import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://fish-keeper.us-west-1.elasticbeanstalk.com"

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(FakeInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface FishKeeperService {
    @GET("catch")
    fun listCatches(): Observable<List<CatchResponse>>

    @POST("catch")
    fun saveCatch(@Body toSave: CatchPost): Observable<CatchResponse>
}

object FishKeeperApi {
    val retrofitService: FishKeeperService by lazy {
        retrofit.create(FishKeeperService::class.java)
    }
}