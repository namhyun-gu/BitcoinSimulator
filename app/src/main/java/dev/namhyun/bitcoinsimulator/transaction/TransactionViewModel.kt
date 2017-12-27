package dev.namhyun.bitcoinsimulator.transaction

import android.arch.lifecycle.ViewModel
import dev.namhyun.bitcoinsimulator.data.local.PurchaseCurrencyDao
import dev.namhyun.bitcoinsimulator.data.local.TransactionHistoryDao
import dev.namhyun.bitcoinsimulator.data.local.WalletDao
import dev.namhyun.bitcoinsimulator.data.local.model.PurchaseCurrency
import dev.namhyun.bitcoinsimulator.data.local.model.Wallet
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.BithumbService
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.BithumbServiceGenerator
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currency
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Ticker
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class TransactionViewModel : ViewModel() {

    private val db: Realm = Realm.getDefaultInstance()

    private val bithumbService: BithumbService = BithumbServiceGenerator.generate()

    fun updatePrice(currencyName: String): Flowable<Ticker<Currency>>
            = Flowable.interval(500, TimeUnit.MILLISECONDS)
            .flatMap { bithumbService.getCurrencyTicker(currencyName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun buyCurrency(currencyName: String, count: Float): Flowable<TransactionStatus> {
        return bithumbService.getCurrencyTicker(currencyName)
                .map { it.data.currentPrice }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { writeBuyTransaction(currencyName, count, it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun sellCurrency(currencyName: String, count: Float): Flowable<TransactionStatus> {
        return bithumbService.getCurrencyTicker(currencyName)
                .map { it.data.currentPrice }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { writeSellTransaction(currencyName, count, it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPurchaseCurrency(currencyName: String): PurchaseCurrency? {
        return PurchaseCurrencyDao(db).findByName(currencyName)
    }

    fun getWallet(): Wallet = WalletDao(db).find()

    private fun writeBuyTransaction(currencyName: String, count: Float, price: Int): TransactionStatus {
        var status: TransactionStatus = TransactionStatus.SUCCESS

        val realm = Realm.getDefaultInstance()
        val walletDao = WalletDao(realm)
        val purchaseCurrencyDao = PurchaseCurrencyDao(realm)
        val transactionHistoryDao = TransactionHistoryDao(realm)

        val wallet = walletDao.find()

        val totalPrice = (price * count).roundToLong()
        val caculatedBudget = wallet.budget - totalPrice

        if (totalPrice < 1000) {
            status = TransactionStatus.LOW_PRICE
        } else if (caculatedBudget > 0) {
            walletDao.update(wallet) {
                it.budget = caculatedBudget
            }

            val purchaseItem = purchaseCurrencyDao.findByName(currencyName)
            if (purchaseItem != null) {
                purchaseCurrencyDao.update(purchaseItem) {
                    it.count += count
                }
            } else {
                purchaseCurrencyDao.create {
                    it.name = currencyName
                    it.count = count
                }
            }

            transactionHistoryDao.create {
                it.date = Date()
                it.type = "buy"
                it.transactionPrice = totalPrice
                it.currencyName = currencyName
                it.currencyCount = count
                it.currencyPrice = price
            }
        } else {
            status = TransactionStatus.NOT_ENOUGH_BUDGET
        }
        realm.close()
        return status
    }

    private fun writeSellTransaction(currencyName: String, count: Float, price: Int): TransactionStatus {
        var status: TransactionStatus = TransactionStatus.SUCCESS

        val realm = Realm.getDefaultInstance()
        val walletDao = WalletDao(realm)
        val purchaseCurrencyDao = PurchaseCurrencyDao(realm)
        val transactionHistoryDao = TransactionHistoryDao(realm)

        val wallet = walletDao.find()

        val totalPrice = (price * count).roundToLong()

        val purchaseItem = purchaseCurrencyDao.findByName(currencyName)

        if (totalPrice < 1000) {
            status = TransactionStatus.LOW_PRICE
        } else if (purchaseItem != null) {
            val caculatedCount = purchaseItem.count - count
            if (caculatedCount < 0) {
                status = TransactionStatus.OVERSELLING
            } else {
                walletDao.update(wallet) {
                    it.budget += totalPrice
                }

                if (caculatedCount == 0F) {
                    purchaseCurrencyDao.delete(purchaseItem)
                } else {
                    purchaseCurrencyDao.update(purchaseItem) {
                        it.count -= count
                    }
                }

                transactionHistoryDao.create {
                    it.date = Date()
                    it.type = "sell"
                    it.transactionPrice = totalPrice
                    it.currencyName = currencyName
                    it.currencyCount = count
                    it.currencyPrice = price
                }
            }
        } else {
            status = TransactionStatus.NOT_EXISTS_ITEM
        }
        realm.close()
        return status
    }

    override fun onCleared() {
        db.close()
        super.onCleared()
    }
}