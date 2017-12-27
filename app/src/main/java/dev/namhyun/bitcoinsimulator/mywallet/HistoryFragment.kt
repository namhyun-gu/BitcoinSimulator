package dev.namhyun.bitcoinsimulator.mywallet

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.data.local.model.TransactionHistory
import kotlinx.android.synthetic.main.recycler_view.view.*

class HistoryFragment : Fragment() {

    private lateinit var viewModel: MyWalletViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.recycler_view, container, false)
        rootView.recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyWalletViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        showHistory(viewModel.getHistory())
    }

    fun showHistory(historyList: List<TransactionHistory>) {
        view?.recycler_view?.adapter = HistoryAdapter(historyList)
    }
}