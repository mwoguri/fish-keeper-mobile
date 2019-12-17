package com.example.fishkeeper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fishkeeper.network.CatchPost
import com.example.fishkeeper.network.CatchResponse
import com.example.fishkeeper.network.FishKeeperApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listCatches: Call<List<CatchResponse>> = FishKeeperApi.retrofitService.listCatches()
        listCatches.enqueue(object : Callback<List<CatchResponse>> {

            override fun onResponse(
                call: Call<List<CatchResponse>>,
                response: Response<List<CatchResponse>>
            ) {
                Log.d(TAG, "listCatches onResponse ${response.body()}")
            }

            override fun onFailure(call: Call<List<CatchResponse>>, t: Throwable) {
                Log.d(TAG, "listCatches onFailure ${t.localizedMessage}")
            }
        })
        val catchToSave = CatchPost(
            77.754500,
            -101.413500,
            4800.00,
            10.00,
            "Brook trout",
            8,
            "Pheasant Tail",
            24,
            1415617165516,
            123
        )
        val postCatch = FishKeeperApi.retrofitService.saveCatch(catchToSave)
        postCatch.enqueue(object : Callback<CatchResponse> {

            override fun onResponse(call: Call<CatchResponse>, response: Response<CatchResponse>) {
                Log.d(TAG, "saveCatch onResponse ${response.body()}")
            }

            override fun onFailure(call: Call<CatchResponse>, t: Throwable) {
                Log.d(TAG, "saveCatch onFailure ${t.localizedMessage}")
            }

        })
    }
}
