package dev.namhyun.bitcoinsimulator.data.local

import dev.namhyun.bitcoinsimulator.data.local.model.Wallet
import io.realm.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.where

class WalletDao(private val realm: Realm) {

    fun create(task: (Wallet) -> Unit): Wallet {
        var wallet = Wallet()
        wallet.let(task)
        realm.executeTransaction {
            wallet = realm.copyToRealm(wallet)
        }
        return wallet
    }

    fun find(): Wallet {
        var wallet = realm.where<Wallet>().findFirst()
        if (wallet == null) {
            wallet = create {}
        }
        return wallet
    }

    fun update(wallet: Wallet, task: (Wallet) -> Unit) {
        realm.executeTransaction {
            wallet.let(task)
            realm.copyToRealmOrUpdate(wallet)
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.delete<Wallet>()
        }
    }
}