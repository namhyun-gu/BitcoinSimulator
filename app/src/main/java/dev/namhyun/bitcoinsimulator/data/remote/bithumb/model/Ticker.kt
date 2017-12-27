package dev.namhyun.bitcoinsimulator.data.remote.bithumb.model

data class Ticker<T>(val status: String, val data: T)