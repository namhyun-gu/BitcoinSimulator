package dev.namhyun.bitcoinsimulator.ticker

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.mywallet.MyWalletActivity
import dev.namhyun.bitcoinsimulator.transaction.TransactionActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_ticker.*

class TickerActivity : AppCompatActivity() {

    companion object {
        val TAG = TickerActivity::class.java.simpleName!!
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var tickerAdapter: TickerAdapter? = null

    private lateinit var viewModel: TickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticker)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(TickerViewModel::class.java)

        transaction_fab.setOnClickListener { _ ->
            startActivity(Intent(this, TransactionActivity::class.java))
        }

        ticker_list.apply {
            layoutManager = LinearLayoutManager(this@TickerActivity)
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && transaction_fab.visibility == View.VISIBLE) {
                        transaction_fab.hide()
                    } else if (dy < 0 && transaction_fab.visibility != View.VISIBLE) {
                        transaction_fab.show()
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val disposable = viewModel.updateTicker().subscribe({
            if (tickerAdapter == null) {
                tickerAdapter = TickerAdapter(it.data)
                ticker_list.adapter = tickerAdapter
            } else {
                tickerAdapter?.updateCurrencies(it.data)
            }
        }, {
            Log.e(TAG, "Can't update ticker", it)
        })
        compositeDisposable.add(disposable)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_wallet -> {
                startActivity(Intent(this, MyWalletActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
