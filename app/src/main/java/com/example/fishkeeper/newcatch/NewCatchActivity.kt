package com.example.fishkeeper.newcatch

import android.Manifest.permission
import android.animation.LayoutTransition
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fishkeeper.R
import com.example.fishkeeper.databinding.ActivityNewCatchBinding
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
private const val REQUEST_LOCATION_PERMISSION = 1

class NewCatchActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: NewCatchViewModel
        get() {
            return ViewModelProviders.of(this)
                .get(NewCatchViewModel::class.java)
        }

    override fun onMapReady(map: GoogleMap?) {
        map?.setOnMapClickListener(viewModel)

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        viewModel.eventUseDeviceLocation.observe(this, Observer { useDeviceLocation ->
            if (useDeviceLocation) {
                hideSoftKeyboard(binding.root)
                useMyLocation()
                viewModel.eventUseDeviceLocationHandled()
            }
        })
    }

    private fun findMyLocationOnMap() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    viewModel.updateLocation(location)
                }
            }
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

    private fun useMyLocation() {
        if (isPermissionGranted()) {
            findMyLocationOnMap()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                findMyLocationOnMap()
            }
        }
    }
}