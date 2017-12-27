package dev.namhyun.bitcoinsimulator.data.remote.bithumb

import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currencies
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currency
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Ticker
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface BithumbService {

    @GET("/public/ticker/ALL")
    fun getAllCurreniesTicker(): Flowable<Ticker<Currencies>>

    @GET("/public/ticker/{currency}")
    fun getCurrencyTicker(@Path("currency") currency: String): Flowable<Ticker<Currency>>

}