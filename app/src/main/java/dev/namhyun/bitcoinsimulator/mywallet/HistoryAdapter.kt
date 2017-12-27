package dev.namhyun.bitcoinsimulator.mywallet

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.data.local.model.TransactionHistory
import dev.namhyun.bitcoinsimulator.extensions.toCurrencyString
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(historyList: List<TransactionHistory>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        val TYPE_SECTION = 100

        val TYPE_ITEM = 101

    }

    private val itemList = arrayListOf<Item>()

    init {
        val itemMap = mutableMapOf<String, ArrayList<TransactionHistory>>()

        historyList.forEach {
            val dateString = DateFormat.format("yyyy. MM. dd", it.date) as String
            if (itemMap.containsKey(dateString)) {
                val list = itemMap[dateString]!!
                list.add(it)
                itemMap.put(dateString, list)
            } else {
                itemMap.put(dateString, arrayListOf(it))
            }
        }

        itemMap.keys.sortedDescending().forEach {
            itemList.add(Item(TYPE_SECTION, null, it))
            itemMap[it]?.sortedByDescending { it.date }?.forEach {
                itemList.add(Item(TYPE_ITEM, it, null))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            TYPE_SECTION ->
                SectionViewHolder(inflater.inflate(R.layout.item_section, parent, false))
            else ->
                HistoryViewHolder(inflater.inflate(R.layout.item_history, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is HistoryViewHolder) {
            holder.bind(itemList[position].history!!)
        } else if (holder is SectionViewHolder) {
            holder.bind(itemList[position].date!!)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(history: TransactionHistory) {
            itemView.transaction_type.text = history.type.toUpperCase()
            itemView.currency_detail.text = "${history.currencyCount} ${history.currencyName}"
            itemView.transaction_date.text = DateUtils.getRelativeTimeSpanString(history.date?.time!!)
            itemView.currency_price.text = history.transactionPrice.toCurrencyString(itemView?.context!!)
        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: String) {
            (itemView as TextView).text = item
        }
    }

    data class Item(val type: Int, val history: TransactionHistory?, val date: String?)
}