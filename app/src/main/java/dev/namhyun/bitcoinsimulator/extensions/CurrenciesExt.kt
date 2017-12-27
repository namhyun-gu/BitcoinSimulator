@file:JvmName("CurrenciesUtils")

package dev.namhyun.bitcoinsimulator.extensions

import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currencies
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currency

fun Currencies.toMap(): Map<Int, Currency> = mapOf(
        R.string.currency_btc to BTC,
        R.string.currency_eth to ETH,
        R.string.currency_dash to DASH,
        R.string.currency_ltc to LTC,
        R.string.currency_etc to ETC,
        R.string.currency_xrp to XRP,
        R.string.currency_bch to BCH,
        R.string.currency_xmr to XMR,
        R.string.currency_zec to ZEC,
        R.string.currency_qtum to QTUM,
        R.string.currency_btg to BTG
)

fun Currencies.toList(): List<Currency> = listOf(
        BTC, ETH, DASH, LTC, ETC, XRP, BCH, XMR, ZEC, QTUM, BTG
)

fun Currencies.size(): Int = toList().size