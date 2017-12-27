package dev.namhyun.bitcoinsimulator.mywallet

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.data.local.model.PurchaseCurrency
import dev.namhyun.bitcoinsimulator.utils.BitcoinResourceUtils
import kotlinx.android.synthetic.main.item_purchase_currency.view.*

class WalletAdapter(private var purchaseCurrencies: List<PurchaseCurrency>) : RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.item_purchase_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(purchaseCurrencies[position])
    }

    override fun getItemCount(): Int = purchaseCurrencies.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(purchaseCurrency: PurchaseCurrency) {
            val iconDrawable =
                    BitcoinResourceUtils.getIconDrawable(itemView.context, purchaseCurrency.name)

            itemView.currency_icon.setImageDrawable(iconDrawable)
            itemView.currency_name.text =
                    BitcoinResourceUtils.getFullName(itemView.context, purchaseCurrency.name)
            itemView.currency_count.text = "${purchaseCurrency.count} ${purchaseCurrency.name}"
        }
    }
}