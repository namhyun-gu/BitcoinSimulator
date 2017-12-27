package dev.namhyun.bitcoinsimulator.ticker

import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currencies
import dev.namhyun.bitcoinsimulator.data.remote.bithumb.model.Currency
import dev.namhyun.bitcoinsimulator.extensions.getChangeRate
import dev.namhyun.bitcoinsimulator.extensions.size
import dev.namhyun.bitcoinsimulator.extensions.toCurrencyString
import dev.namhyun.bitcoinsimulator.extensions.toMap
import dev.namhyun.bitcoinsimulator.utils.BitcoinResourceUtils
import kotlinx.android.synthetic.main.item_currency.view.*

class TickerAdapter(private var currencies: Currencies) : RecyclerView.Adapter<TickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.item_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val map = currencies.toMap()
        val key = map.keys.toList()[position]
        val currency = map[key]

        currency?.let {
            holder?.bind(key, it)
        }
    }

    override fun getItemCount(): Int = currencies.size()

    fun updateCurrencies(newCurrencies: Currencies) {
        currencies = newCurrencies
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(@StringRes name: Int, currency: Currency) {
            val icon = BitcoinResourceUtils.getIconDrawable(itemView.context, name)
            itemView.currency_icon.setImageDrawable(icon)

            itemView.currency_name.text = itemView.context.getString(name)

            val currentPrice = currency.currentPrice
            itemView.currency_price.text = currentPrice.toCurrencyString(itemView.context)

            val diffPrice = currentPrice - currency.openingPrice

            if (diffPrice >= 0) {
                itemView.currency_change_rate.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.increasePriceColor))
                itemView.currency_state_arrow.setImageResource(R.drawable.ic_price_increase_24dp)
            } else {
                itemView.currency_change_rate.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.decreasePriceColor))
                itemView.currency_state_arrow.setImageResource(R.drawable.ic_price_decrease_24dp)
            }

            itemView.currency_change_rate.text = "${diffPrice.toCurrencyString(itemView.context)} (${"%.2f".format(currency.getChangeRate())} %)"
        }
    }
}