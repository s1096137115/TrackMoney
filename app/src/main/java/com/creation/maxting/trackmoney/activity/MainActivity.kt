package com.creation.maxting.trackmoney.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.creation.maxting.trackmoney.R
import com.creation.maxting.trackmoney.data.InvoiceAction
import com.creation.maxting.trackmoney.data.InvoiceType
import com.creation.maxting.trackmoney.data.QueryInvoiceHeader
import com.creation.maxting.trackmoney.utils.RetrofitClient
import com.creation.maxting.trackmoney.utils.SystemUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    val mFragments: List<Fragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPager()
        apiForSingle()
        println("<<<<<<<<<<uuid:${SystemUtils.getUUID()}")
    }

    private fun apiForSingle() {
        val queryInvoiceHeader = QueryInvoiceHeader(0.3f, InvoiceType.QRCode.name,
                "UJ65256975", InvoiceAction.qryInvHeader.name, "V2", "2017/05/06", SystemUtils.getUUID())

        val fields: Map<String, String> = SystemUtils.pojo2Map(queryInvoiceHeader)

        RetrofitClient.getEInvoiceAPI(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            println("<<<<<<<<<<<<<<<<${it}")
                        },
                        {
                            error ->
                            println("<<<<<<<<<<<<error:${error.message}")
                        })
    }

    private fun setViewPager() {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = mFragments[position]
            override fun getCount() = mFragments.size
        }
    }
}

