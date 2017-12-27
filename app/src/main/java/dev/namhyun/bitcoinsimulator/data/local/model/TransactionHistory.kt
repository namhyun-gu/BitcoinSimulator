package dev.namhyun.bitcoinsimulator.data.local.model

import io.realm.RealmObject
import java.util.*

open class TransactionHistory : RealmObject() {

    var date: Date? = null

    var type: String = ""

    var transactionPrice: Long = 0

    var currencyName: String = ""

    var currencyPrice: Int = 0

    var currencyCount: Float = 0F

}