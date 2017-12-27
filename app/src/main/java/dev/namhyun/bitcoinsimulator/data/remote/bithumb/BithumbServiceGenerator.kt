package dev.namhyun.bitcoinsimulator.data.remote.bithumb

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object BithumbServiceGenerator {

    fun generate(): BithumbService = buildRetrofit().create(BithumbService::class.java)

    private fun buildRetrofit() = Retrofit.Builder()
            .baseUrl("https://api.bithumb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

}