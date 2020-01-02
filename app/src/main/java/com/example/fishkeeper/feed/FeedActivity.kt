package com.example.fishkeeper.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fishkeeper.newcatch.NewCatchActivity
import com.example.fishkeeper.R
import com.example.fishkeeper.databinding.ActivityFeedBinding

private const val TAG = "FeedActivity"

class FeedActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        val binding: ActivityFeedBinding = DataBindingUtil.setContentView(this, R.layout.activity_feed)

        val feedViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        binding.viewmodel = feedViewModel
        binding.lifecycleOwner = this

        val adapter = CatchAdapter()
        binding.catchFeed.adapter = adapter

        feedViewModel.catchesList.observe(this, Observer { list ->
            Log.d(TAG, list.toString())
            adapter.submitList(list)
        })

        feedViewModel.eventAddCatch.observe(this, Observer { clicked ->
            if (clicked) {
                Log.d(TAG, "clicked: $clicked")
                startActivity(Intent(this, NewCatchActivity::class.java))
                feedViewModel.eventAddCatchHandled()
            }
        })


//        val catchToSave = CatchPost(
//            77.754500,
//            -101.413500,
//            4800.00,
//            10.00,
//            "Brook trout",
//            8,
//            "Pheasant Tail",
//            24,
//            1415617165516
//        )
//        val postCatchObservable = FishKeeperApi.retrofitService.saveCatch(catchToSave)
//        val catchPostDisposable = postCatchObservable
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(object : DisposableObserver<CatchResponse>() {
//                override fun onComplete() {
//                    Log.d(TAG, "postCatch onComplete")
//                }
//
//                override fun onNext(t: CatchResponse) {
//                    Log.d(TAG, "postCatch onNext $t")
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d(TAG, "postCatch onError ${e.localizedMessage}")
//                }
//
//            })
//        compositeDisposable.add(catchPostDisposable)

    }


}
