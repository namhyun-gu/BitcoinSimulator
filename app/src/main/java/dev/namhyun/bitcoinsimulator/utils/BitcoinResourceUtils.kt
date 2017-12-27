package dev.namhyun.bitcoinsimulator.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import dev.namhyun.bitcoinsimulator.R

object BitcoinResourceUtils {

    fun getIconDrawable(context: Context, @StringRes nameRes: Int): Drawable? {
        return getSimpleName(nameRes)?.let { getIconDrawable(context, it) }
    }

    fun getIconDrawable(context: Context, name: String): Drawable? {
        val iconDrawableIds = when (name) {
            "BTC" -> R.drawable.ic_btc
            "ETH" -> R.drawable.ic_eth
            "DASH" -> R.drawable.ic_dash
            "LTC" -> R.drawable.ic_ltc
            "ETC" -> R.drawable.ic_etc
            "XRP" -> R.drawable.ic_xrp
            "BCH" -> R.drawable.ic_bch
            "XMR" -> R.drawable.ic_xmr
            "ZEC" -> R.drawable.ic_zec
            "QTUM" -> R.drawable.ic_qtum
            "BTG" -> R.drawable.ic_btg
            else -> null
        }

        return iconDrawableIds?.let { context.getDrawable(it) }
    }

    fun getSimpleName(@StringRes nameRes: Int) : String? {
        return when (nameRes) {
            R.string.currency_btc -> "BTC"
            R.string.currency_eth -> "ETH"
            R.string.currency_dash -> "DASH"
            R.string.currency_ltc -> "LTC"
            R.string.currency_etc -> "ETC"
            R.string.currency_xrp -> "XRP"
            R.string.currency_bch -> "BCH"
            R.string.currency_xmr -> "XMR"
            R.string.currency_zec -> "ZEC"
            R.string.currency_qtum -> "QTUM"
            R.string.currency_btg -> "BTG"
            else -> null
        }
    }

    fun getFullName(context: Context, name: String): String? {
        val stringResourceIds = when (name) {
            "BTC" -> R.string.currency_btc
            "ETH" -> R.string.currency_eth
            "DASH" -> R.string.currency_dash
            "LTC" -> R.string.currency_ltc
            "ETC" -> R.string.currency_etc
            "XRP" -> R.string.currency_xrp
            "BCH" -> R.string.currency_bch
            "XMR" -> R.string.currency_xmr
            "ZEC" -> R.string.currency_zec
            "QTUM" -> R.string.currency_qtum
            "BTG" -> R.string.currency_btg
            else -> null
        }

        return stringResourceIds?.let { context.getString(it) }
    }
}