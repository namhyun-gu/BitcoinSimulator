package dev.namhyun.bitcoinsimulator.data.remote.bithumb.model

import com.google.gson.annotations.SerializedName

data class Currency(@SerializedName("closing_price") val currentPrice: Int,
                    @SerializedName("opening_price") val openingPrice: Int)