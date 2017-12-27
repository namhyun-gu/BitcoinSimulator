package dev.namhyun.bitcoinsimulator.data.local.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PurchaseCurrency : RealmObject() {

    @PrimaryKey var name: String = ""

    var count: Float = 0F

}