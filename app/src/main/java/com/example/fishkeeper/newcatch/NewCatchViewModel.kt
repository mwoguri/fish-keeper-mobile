package com.example.fishkeeper.newcatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishkeeper.R
import com.example.fishkeeper.network.CatchPost
import com.example.fishkeeper.network.CatchResponse
import com.example.fishkeeper.network.FishKeeperApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.lang.Float.parseFloat

private const val TAG = "NewCatchViewModel"

class NewCatchViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    //////////////////////////////////////////////////////////
    // Species: required Stringfield
    //////////////////////////////////////////////////////////
    val species = MutableLiveData<String>()
    private val _speciesError = MutableLiveData<Int?>()
    val speciesError: LiveData<Int?>
        get() = _speciesError

    //////////////////////////////////////////////////////////
    // Lure/fly type: required String field
    //////////////////////////////////////////////////////////
    val lure = MutableLiveData<String>()
    private val _lureError = MutableLiveData<Int?>()
    val lureError: LiveData<Int?>
        get() = _lureError

    //////////////////////////////////////////////////////////
    // Hook size: required Int field
    //////////////////////////////////////////////////////////
    val hookSize = MutableLiveData<Int?>()
    private val _hookSizeError = MutableLiveData<Int?>()
    val hookSizeError: LiveData<Int?>
        get() = _hookSizeError

    //////////////////////////////////////////////////////////
    // Length: optional Float field (String for now...)
    //////////////////////////////////////////////////////////
    val length = MutableLiveData<String>() //TODO make a float
    private val _lengthError = MutableLiveData<Int?>()
    val lengthError: LiveData<Int?>
        get() = _lengthError

    //////////////////////////////////////////////////////////
    // Weight: optional Float field (String for now...)
    //////////////////////////////////////////////////////////
    val weight = MutableLiveData<String>() //TODO make a float
    private val _weightError = MutableLiveData<Int?>()
    val weightError: LiveData<Int?>
        get() = _weightError


    //////////////////////////////////////////////////////////
    // Weight: optional Float field (String for now...)
    //////////////////////////////////////////////////////////
    private val _postComplete = MutableLiveData<Boolean?>()
    val postComplete: LiveData<Boolean?>
        get() = _postComplete

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun eventSubmit() {
        _eventSubmit.value = true
    }

    fun eventSubmitHandled() {
        _eventSubmit.value = false
    }

    fun submitCatch() {
        Log.d(TAG, "species: ${species.value}")
        Log.d(TAG, "hook: ${hookSize.value}")
        val isValid = validateData()
        if (isValid) {
            sendCatch()
        }
    }

    private fun sendCatch() {

        if (species.value == null || lure.value == null || hookSize.value == null) {
            return
        }

        val newCatch = CatchPost(
            null, // TODO add lat
            null, // TODO add long
            null, // TODO add altitude
            length.value?.toDoubleOrNull(),
            species.value!!,
            weight.value?.toIntOrNull(),
            lure.value!!,
            hookSize.value!!,
            System.currentTimeMillis()


        )
        val listCatchesObservable: Observable<CatchResponse> =
            FishKeeperApi.retrofitService.saveCatch(newCatch)

        val disposable = listCatchesObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<CatchResponse>() {
                override fun onComplete() {
                    Log.d(TAG, "onComplete")
                }

                override fun onNext(t: CatchResponse) {
                    Log.d(TAG, t.toString())
                    _postComplete.value = true
                    clearValues()
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "error: ${e.localizedMessage}")
                    _postComplete.value = false
                }

            })

        compositeDisposable.add(disposable)
    }

    private fun clearValues() {
        species.value = ""
        lure.value = ""
        hookSize.value = null
        length.value = ""
        weight.value = ""
    }

    private fun validateData(): Boolean {
        var isValid = true
        if (species.value.isNullOrBlank()) {
            _speciesError.value = R.string.required_field
            isValid = false
        } else {
            _speciesError.value = null
        }
        if (lure.value.isNullOrBlank()) {
            _lureError.value = R.string.required_field
            isValid = false
        } else {
            _lureError.value = null
        }

        if (hookSize.value == null) {
            _hookSizeError.value = R.string.required_field
            isValid = false
        } else {
            _hookSizeError.value = null
        }

        if (isInvalidFloat(length.value)) {
            _lengthError.value = R.string.invalid_value
            isValid = false
        } else {
            _lengthError.value = null
        }

        if (isInvalidFloat(weight.value)) {
            _weightError.value = R.string.invalid_value
            isValid = false
        } else {
            _weightError.value = null
        }

        return isValid
    }

    private fun isInvalidFloat(value: String?): Boolean {
        if (value == null || value == "") return false
        return try {
            parseFloat(value)
            false
        } catch (e: Throwable) {
            true
        }
    }

    fun postCompleteHandled() {
        _postComplete.value = null
    }
}