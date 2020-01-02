package com.example.fishkeeper.newcatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "NewCatchViewModel"

class NewCatchViewModel : ViewModel() {
    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    val species = MutableLiveData<String>()

    fun eventSubmit() {
        _eventSubmit.value = true
    }

    fun eventSubmitHandled() {
        _eventSubmit.value = false
    }

    fun submitCatch() {
        Log.d(TAG, "species: ${species.value}")
    }
}