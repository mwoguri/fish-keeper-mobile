package com.example.fishkeeper.newcatch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fishkeeper.R
import com.example.fishkeeper.databinding.ActivityNewCatchBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_new_catch.*


private const val TAG = "NewCatchActivity"

class NewCatchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityNewCatchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_catch)

        val viewModel = ViewModelProviders.of(this)
            .get(NewCatchViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.eventSubmit.observe(this, Observer { clicked ->
            Log.d(TAG, "clicked: $clicked")
            if (clicked) {
                viewModel.submitCatch()
                viewModel.eventSubmitHandled()
            }
        })

        viewModel.speciesError.observe(this, Observer { stringId ->
            updateErrorMessage(stringId, speciesInputLayout)
        })

        viewModel.lureError.observe(this, Observer { stringId ->
            updateErrorMessage(stringId, lureInputLayout)
        })

        viewModel.hookSizeError.observe(this, Observer { stringId ->
            updateErrorMessage(stringId, hookInputLayout)
        })

        viewModel.lengthError.observe(this, Observer { stringId ->
            updateErrorMessage(stringId, lengthInputLayout)
        })

        viewModel.weightError.observe(this, Observer { stringId ->
            updateErrorMessage(stringId, weightInputLayout)
        })

        viewModel.postComplete.observe(this, Observer { isComplete ->
            hideSoftKeyboard(binding.root)
            isComplete?.let {
                if (isComplete) {
                    binding.species.requestFocus()
                    Snackbar.make(
                        binding.root,
                        R.string.catch_saved,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Snackbar.make(
                        binding.root,
                        R.string.network_error,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                viewModel.postCompleteHandled()
            }
        })

    }

    private fun updateErrorMessage(stringId: Int?, textInputLayout: TextInputLayout) {
        if (stringId != null) {
            textInputLayout.error = getString(stringId)
        } else {
            textInputLayout.error = null
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}