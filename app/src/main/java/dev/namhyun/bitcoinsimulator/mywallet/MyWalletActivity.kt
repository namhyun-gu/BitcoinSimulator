package dev.namhyun.bitcoinsimulator.mywallet

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import dev.namhyun.bitcoinsimulator.R
import dev.namhyun.bitcoinsimulator.data.local.model.Wallet
import dev.namhyun.bitcoinsimulator.extensions.toCurrencyString
import kotlinx.android.synthetic.main.activity_mywallet.*

class MyWalletActivity : AppCompatActivity(),
        TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private lateinit var viewModel: MyWalletViewModel

    private lateinit var pagerAdapter: MyWalletPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mywallet)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(MyWalletViewModel::class.java)

        pagerAdapter = MyWalletPagerAdapter(supportFragmentManager)

        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(this)

        tab_layout.addOnTabSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        showBudget(viewModel.getWallet())
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        view_pager.setCurrentItem(tab?.position!!, true)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onPageSelected(position: Int) {
        tab_layout.getTabAt(position)?.select()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
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

    fun showBudget(wallet: Wallet) {
        current_budget.text = wallet.budget.toCurrencyString(this)
    }

    inner class MyWalletPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> WalletFragment()
                1 -> HistoryFragment()
                else -> null
            }
        }

        override fun getCount(): Int = 2
    }
}
