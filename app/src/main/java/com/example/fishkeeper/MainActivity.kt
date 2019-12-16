package com.example.fishkeeper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fishkeeper.network.Catch
import com.example.fishkeeper.network.FishKeeperApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listCatches: Call<List<Catch>> = FishKeeperApi.retrofitService.listCatches()
        listCatches.enqueue(object : Callback<List<Catch>> {

            override fun onResponse(call: Call<List<Catch>>, response: Response<List<Catch>>) {
                Log.d("MainActivity", "onResponse ${response.body()}")
            }

            override fun onFailure(call: Call<List<Catch>>, t: Throwable) {
                Log.d("MainActivity", "onFailure ${t.localizedMessage}")
            }
        })
    }
}
