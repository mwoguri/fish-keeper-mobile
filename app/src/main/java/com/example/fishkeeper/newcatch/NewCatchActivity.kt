package com.example.fishkeeper.newcatch

import android.animation.LayoutTransition
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fishkeeper.R
import com.example.fishkeeper.databinding.ActivityNewCatchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_new_catch.*

private const val TAG = "NewCatchActivity"

class NewCatchActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var map: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: NewCatchViewModel
        get() {
            return ViewModelProviders.of(this)
                .get(NewCatchViewModel::class.java)
        }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            it.setOnMapClickListener(viewModel)
            this.map = map
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val binding: ActivityNewCatchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_catch)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        (binding.constraintLayout as ViewGroup).layoutTransition.enableTransitionType(
            LayoutTransition.CHANGING
        )

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        viewModel.eventSubmit.observe(this, Observer { clicked ->
            Log.d(TAG, "clicked: $clicked")
            if (clicked) {
                viewModel.closeMap()
                viewModel.submitCatch()
                viewModel.eventSubmitHandled()
            }
        })

        viewModel.mapFullScreen.observe(this, Observer { isFullScreen ->
            val mapParams = binding.mapLayout.layoutParams as ConstraintLayout.LayoutParams

            if (isFullScreen) {
                hideSoftKeyboard(binding.root)
                supportActionBar?.hide()
                mapParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                mapParams.topToBottom = ConstraintLayout.LayoutParams.UNSET
                mapParams.matchConstraintPercentHeight = 0.9f
            } else {
                supportActionBar?.show()
                mapParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                mapParams.topToBottom = R.id.weightInputLayout
                mapParams.matchConstraintPercentHeight = 1f
            }
            binding.mapLayout.layoutParams = mapParams
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

        viewModel.latLng.observe(this, Observer { locationUpdate ->
            map?.let {
                it.clear()
                it.addMarker(
                    MarkerOptions().position(locationUpdate.latLng)
                )
                if (locationUpdate.isUserInputted) {
                    it.animateCamera(CameraUpdateFactory.newLatLng(locationUpdate.latLng))
                } else {
                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(locationUpdate.latLng, 15f))
                }
            }
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

        viewModel.eventUseDeviceLocation.observe(this, Observer { doLocationUpdate ->
            if (doLocationUpdate) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            viewModel.updateLocation(location)
                        }
                    }
                viewModel.locationUpdated()
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