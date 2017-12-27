package dev.namhyun.bitcoinsimulator.data.local

import dev.namhyun.bitcoinsimulator.data.local.model.TransactionHistory
import io.realm.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.where

class TransactionHistoryDao(private val realm: Realm) {

    fun findAll(): List<TransactionHistory> = realm.where<TransactionHistory>()
            .findAll().toList()

    fun create(task: (TransactionHistory) -> Unit): TransactionHistory {
        var history = TransactionHistory()
        history.let(task)
        realm.executeTransaction {
            history = realm.copyToRealm(history)
        }
        return history
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.delete<TransactionHistory>()
        }
    }
}