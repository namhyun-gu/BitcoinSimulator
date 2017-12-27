package dev.namhyun.bitcoinsimulator.data.local

import dev.namhyun.bitcoinsimulator.data.local.model.PurchaseCurrency
import io.realm.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.where

class PurchaseCurrencyDao(private val realm: Realm) {

    fun create(task: (PurchaseCurrency) -> Unit): PurchaseCurrency {
        var currency = PurchaseCurrency()
        currency.let(task)
        realm.executeTransaction {
            currency = realm.copyToRealm(currency)
        }
        return currency
    }

    fun findAll(): List<PurchaseCurrency> {
        return realm.where<PurchaseCurrency>().findAll().toList()
    }

    fun findByName(currencyName: String): PurchaseCurrency? {
        return realm.where<PurchaseCurrency>().equalTo("name", currencyName).findFirst()
    }

    fun update(purchaseCurrency: PurchaseCurrency, task: (PurchaseCurrency) -> Unit): PurchaseCurrency {
        realm.executeTransaction {
            purchaseCurrency.let(task)
            realm.copyToRealmOrUpdate(purchaseCurrency)
        }
        return purchaseCurrency
    }

    fun delete(purchaseCurrency: PurchaseCurrency) {
        realm.executeTransaction {
            purchaseCurrency.deleteFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.delete<PurchaseCurrency>()
        }
    }
}