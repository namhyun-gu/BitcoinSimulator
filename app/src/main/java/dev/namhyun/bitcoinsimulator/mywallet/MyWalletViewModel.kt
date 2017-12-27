package dev.namhyun.bitcoinsimulator.mywallet

import android.arch.lifecycle.ViewModel
import dev.namhyun.bitcoinsimulator.data.local.PurchaseCurrencyDao
import dev.namhyun.bitcoinsimulator.data.local.TransactionHistoryDao
import dev.namhyun.bitcoinsimulator.data.local.WalletDao
import dev.namhyun.bitcoinsimulator.data.local.model.PurchaseCurrency
import dev.namhyun.bitcoinsimulator.data.local.model.TransactionHistory
import dev.namhyun.bitcoinsimulator.data.local.model.Wallet
import io.realm.Realm

class MyWalletViewModel : ViewModel() {

    private val db: Realm = Realm.getDefaultInstance()

    fun getHistory(): List<TransactionHistory>
            = TransactionHistoryDao(db).findAll()

    fun getWallet(): Wallet = WalletDao(db).find()

    fun getPurchaseCurrencies(): List<PurchaseCurrency> = PurchaseCurrencyDao(db).findAll()

    override fun onCleared() {
        db.close()
        super.onCleared()
    }
}