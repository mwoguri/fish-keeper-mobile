package com.example.fishkeeper.newcatch

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fishkeeper.R
import com.example.fishkeeper.databinding.ActivityNewCatchBinding

private const val TAG = "NewCatchActivity"

class NewCatchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val binding: ActivityNewCatchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_catch)

        val viewModel = ViewModelProviders.of(this)
            .get(NewCatchViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this



        viewModel.eventSubmit.observe(this, Observer { clicked ->
            Log.d(TAG, "clicked: $clicked")
            if (clicked) {
                viewModel.submitCatch()
                viewModel.eventSubmitHandled()
            }
        })

    }
}