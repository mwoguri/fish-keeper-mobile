package com.example.fishkeeper.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fishkeeper.R
import com.example.fishkeeper.databinding.ActivityFeedBinding
import com.example.fishkeeper.newcatch.NewCatchActivity

private const val TAG = "FeedActivity"

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFeedBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_feed)

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
    }
}
