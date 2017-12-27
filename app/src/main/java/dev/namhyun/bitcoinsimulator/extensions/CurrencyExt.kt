@file:JvmName("CurrencyUtils")

package dev.namhyun.bitcoinsimulator.extensions

import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currency

fun Currency.getChangeRate(): Float = ((currentPrice - openingPrice) / openingPrice.toFloat()) * 100