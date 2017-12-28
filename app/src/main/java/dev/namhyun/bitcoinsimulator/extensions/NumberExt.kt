@file:JvmName("NumberUtils")

package dev.namhyun.bitcoinsimulator.extensions

import android.content.Context
import dev.namhyun.bitcoinsimulator.R
import java.text.DecimalFormat

fun Number.toCurrencyString(context: Context): String
        = DecimalFormat(context.getString(R.string.krw_price_format)).format(this)
