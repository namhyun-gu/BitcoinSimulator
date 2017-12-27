package dev.namhyun.bitcoinsimulator.data.remote.bithumb.model

import com.google.gson.annotations.SerializedName

data class Currencies(@SerializedName("date") val updateDate: String,
                      val BTC: Currency, val ETH: Currency,
                      val DASH: Currency, val LTC: Currency,
                      val ETC: Currency, val XRP: Currency,
                      val BCH: Currency, val XMR: Currency,
                      val ZEC: Currency, val QTUM: Currency,
                      val BTG: Currency)