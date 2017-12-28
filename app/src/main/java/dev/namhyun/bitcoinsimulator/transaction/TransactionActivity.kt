package dev.namhyun.bitcoinsimulator.transaction

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.extensions.getChangeRate
import dev.namhyun.bitcoinsimulator.extensions.toCurrencyString
import dev.namhyun.bitcoinsimulator.extensions.vaildInput
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.content_transaction.*
import kotlin.math.roundToLong

class TransactionActivity : AppCompatActivity(),
        AdapterView.OnItemSelectedListener {

    companion object {

        val TAG: String = TransactionActivity::class.java.simpleName

    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private lateinit var viewModel: TransactionViewModel

    private lateinit var currencyList: Array<String>

    private lateinit var currentCurrency: String

    private var currentPrice: Int = 0

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        currencyList = resources.getStringArray(R.array.currency_types)

        currentCurrency = currencyList[0]

        currency_selector.apply {
            setSelection(0)
            onItemSelectedListener = this@TransactionActivity
        }

        btn_buy.setOnClickListener {
            executeBuyTransaction()
        }

        btn_sell.setOnClickListener {
            executeSellTransaction()
        }

        input_coin_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePrice()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        startTicker()
        updateBudget()
        updateCoinCount()
    }

    override fun onPause() {
        super.onPause()
        stopTicker()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        currentCurrency = currencyList[position]
        stopTicker()
        startTicker()
        updateCoinCount()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // No-op
    }

    @SuppressLint("SetTextI18n")
    private fun startTicker() {
        val disposable = viewModel.updatePrice(currentCurrency).subscribe({
            val (current, opening) = it.data
            val diffPrice = current - opening
            currentPrice = current

            currency_price.text = current.toCurrencyString(this)
            currency_change_rate.text =
                    "${diffPrice.toCurrencyString(this)} (${"%.2f".format(it.data.getChangeRate())} %)"

            calculatePrice()
            calculateAvailableCoin()
        }, {
            Log.e(TAG, "Can't update currency price", it)
        })
        compositeDisposable.add(disposable)
    }

    private fun stopTicker() {
        compositeDisposable.clear()
    }

    private fun updateBudget() {
        current_budget.text = viewModel.getWallet().budget.toCurrencyString(this)
    }

    @SuppressLint("SetTextI18n")
    private fun updateCoinCount() {
        val purchaseCurrency = viewModel.getPurchaseCurrency(currentCurrency)
        if (purchaseCurrency != null) {
            coin_count.text = purchaseCurrency.count.toString()
        } else {
            coin_count.text = "0"
        }
    }

    private fun calculatePrice() {
        val value = input_coin_count.vaildInput()?.toFloat()
        value?.let {
            val calculatedPrice = (it * currentPrice).roundToLong()
            total_price.text = calculatedPrice.toCurrencyString(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateAvailableCoin() {
        val wallet = viewModel.getWallet()
        if (wallet.budget != 0L) {
            val availableCoin = wallet.budget.toDouble() / currentPrice
            available_coin_count.text = "%.5f".format(availableCoin)
        }
    }

    private fun processTransaction(status: TransactionStatus) {
        when (status) {
            TransactionStatus.SUCCESS -> {
                updateBudget()
                updateCoinCount()
                Snackbar.make(content_frame, "Transaction success", Snackbar.LENGTH_SHORT).show()
            }
            TransactionStatus.OVERSELLING -> {
                Snackbar.make(content_frame, "Overselling", Snackbar.LENGTH_SHORT).show()
            }
            TransactionStatus.NOT_EXISTS_ITEM -> {
                Snackbar.make(content_frame, "Not exists item", Snackbar.LENGTH_SHORT).show()
            }
            TransactionStatus.NOT_ENOUGH_BUDGET -> {
                Snackbar.make(content_frame, "Not enough budget", Snackbar.LENGTH_SHORT).show()
            }
            TransactionStatus.LOW_PRICE -> {
                Snackbar.make(content_frame, "Low price", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun executeBuyTransaction() {
        val count = input_coin_count.vaildInput()?.toFloat()
        count?.let {
            viewModel.buyCurrency(currentCurrency, it).subscribe({
                processTransaction(it)
            }, {
                Log.e(TAG, "Can't buy currency", it)
            })
        }
        hideKeyboard()
    }

    @SuppressLint("CheckResult")
    private fun executeSellTransaction() {
        val count = input_coin_count.vaildInput()?.toFloat()
        count?.let {
            viewModel.sellCurrency(currentCurrency, it).subscribe({
                processTransaction(it)
            }, {
                Log.e(TAG, "Can't sell currency", it)
            })
        }
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input_coin_count.windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
