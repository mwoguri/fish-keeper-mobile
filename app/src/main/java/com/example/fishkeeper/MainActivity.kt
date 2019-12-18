package com.example.fishkeeper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fishkeeper.network.CatchPost
import com.example.fishkeeper.network.CatchResponse
import com.example.fishkeeper.network.FishKeeperApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "listCatches onError ${e.localizedMessage}")
                }

            })

        compositeDisposable.add(listCatchesDisposable)

        val catchToSave = CatchPost(
            77.754500,
            -101.413500,
            4800.00,
            10.00,
            "Brook trout",
            8,
            "Pheasant Tail",
            24,
            1415617165516
        )
        val postCatchObservable = FishKeeperApi.retrofitService.saveCatch(catchToSave)
        val catchPostDisposable = postCatchObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<CatchResponse>() {
                override fun onComplete() {
                    Log.d(TAG, "postCatch onComplete")
                }

                override fun onNext(t: CatchResponse) {
                    Log.d(TAG, "postCatch onNext $t")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "postCatch onError ${e.localizedMessage}")
                }

            })
        compositeDisposable.add(catchPostDisposable)

    }

    override fun onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose()
    }
}
