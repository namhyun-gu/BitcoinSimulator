package dev.namhyun.bitcoinsimulator.data.local.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Wallet : RealmObject() {

    @PrimaryKey var id = 0

    var budget: Long = 100000000

}