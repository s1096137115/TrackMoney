package com.creation.maxting.trackmoney.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.creation.maxting.trackmoney.R
import com.creation.maxting.trackmoney.data.*
import com.creation.maxting.trackmoney.utils.DateUtils
import com.creation.maxting.trackmoney.utils.RetrofitClient
import com.creation.maxting.trackmoney.utils.SystemUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_invoice_detail.*
import kotlinx.android.synthetic.main.activity_invoice_detail.view.*
import kotlinx.android.synthetic.main.view_item_invoice_detail.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MAX on 2017/8/17.
 */
class InvoiceDetailActivity : BaseActivity() {
    private val invoiceDetailAdapter = InvoiceDetailAdapter()
    private var mQRCodeInvoice: QRCodeInvoice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_detail)
        getData()
        setRecyclerView()
    }

    private fun getData() {
        if (intent.extras != null) {
            val invNum = intent.extras.getString(QRCodeInvoice.INVOICE_NUMBER)
            mQRCodeInvoice = mDB.getInvoice(invNum)
            mQRCodeInvoice?.details = mDB.getInvoiceDetails(invNum)
            invoiceDetailAdapter.add(mQRCodeInvoice?.details as MutableList<InvoiceDetail>)
            showDetail(mQRCodeInvoice)
        }
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = invoiceDetailAdapter
    }

    private fun showDetail(invoice: QRCodeInvoice?) {
        sellerName.text = invoice?.sellerName
        invNum.text = invoice?.invNum
        invPeriod.text = DateUtils.getPeriod(invoice?.invPeriod!!)
        invDate.text = DateUtils.dateFormat(invoice.invDate)
        sellerAddress.text = invoice.sellerAddress
        total.text = "總計: ${invoice.total.toString()} 元"
    }
}

class InvoiceDetailAdapter : RecyclerView.Adapter<InvoiceDetailViewHolder>() {
    private var list: MutableList<InvoiceDetail> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_invoice_detail, parent, false)
        return InvoiceDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceDetailViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    fun add(list: MutableList<InvoiceDetail>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun update(list: MutableList<InvoiceDetail>) {
        this.list.clear()
        this.list = list
        notifyDataSetChanged()
    }
}

class InvoiceDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(detail: InvoiceDetail) {
        with(itemView) {
            description.text = detail.description
            quantity.text = detail.quantity
            amount.text = detail.amount
        }
    }
}
