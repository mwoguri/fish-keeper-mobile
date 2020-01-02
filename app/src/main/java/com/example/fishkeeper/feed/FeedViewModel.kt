package com.example.fishkeeper.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishkeeper.network.CatchResponse
import com.example.fishkeeper.network.FishKeeperApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "FeedViewModel"

class FeedViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _catchesList = MutableLiveData<List<CatchResponse>>()
    val catchesList: LiveData<List<CatchResponse>>
        get() = _catchesList

    private val _addCatchEvent = MutableLiveData<Boolean>()
    val eventAddCatch: LiveData<Boolean>
        get() = _addCatchEvent


    init {
        getCatches()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun eventAddCatch() {
        _addCatchEvent.value = true
    }

    fun eventAddCatchHandled() {
        _addCatchEvent.value = false
    }

    private fun getCatches() {
        val listCatchesObservable: Observable<List<CatchResponse>> =
            FishKeeperApi.retrofitService.listCatches()

        val listCatchesDisposable = listCatchesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<CatchResponse>>() {
                override fun onComplete() {
                    Log.d(TAG, "listCatches onComplete")
                }

                override fun onNext(t: List<CatchResponse>) {
                    Log.d(TAG, "listCatches onNext $t")
                    _catchesList.value = t
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "listCatches onError ${e.localizedMessage}")
                }

            })

        compositeDisposable.add(listCatchesDisposable)
    }
}