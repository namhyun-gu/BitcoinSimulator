package dev.namhyun.bitcoinsimulator.ticker

import android.arch.lifecycle.ViewModel
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.BithumbService
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.BithumbServiceGenerator
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currencies
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Ticker
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit

class TickerViewModel : ViewModel() {

    private val bithumbService: BithumbService = BithumbServiceGenerator.generate()

    private val db: Realm = Realm.getDefaultInstance()

    fun updateTicker(): Flowable<Ticker<Currencies>>
        = Flowable.interval(500, TimeUnit.MILLISECONDS)
            .flatMap { bithumbService.getAllCurreniesTicker() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}