package com.creation.maxting.trackmoney.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.creation.maxting.trackmoney.R
import com.creation.maxting.trackmoney.data.InvoiceType
import com.creation.maxting.trackmoney.data.QRCodeInvoice
import com.creation.maxting.trackmoney.data.QueryCarrierAggregate
import com.creation.maxting.trackmoney.data.QueryInvoiceDetail
import com.creation.maxting.trackmoney.utils.DateUtils
import com.creation.maxting.trackmoney.utils.RetrofitClient
import com.creation.maxting.trackmoney.utils.SystemUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_item_list_invoice.view.*

class MainActivity : BaseActivity() {
    private val listInvoiceAdapter = ListInvoiceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        setFab()
//        apiForSingle()
//        apiDetail()
    }



    override fun onResume() {
        super.onResume()
        listInvoiceAdapter.update(mDB.getInvoices())
        apiForSingle()
    }

    private fun apiForSingle() {
        val query = QueryCarrierAggregate("0000000001", "/B95QB+P", "rpkp7nl4")
        val fields: Map<String, String> = SystemUtils.pojo2Map(query)
        RetrofitClient.getEInvoiceAPI()
                .QueryCarrierAggregate(fields)
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

    private fun apiDetail() {
        val queryInvoiceDetail = QueryInvoiceDetail(InvoiceType.QRCode.name, "UJ65276333", "10606",
                "2017/06/29", "5MaIynpBIR9c03JiHSJfLg==", "53658095", "4750")

        val queryInvoiceDetail2 = QueryInvoiceDetail(InvoiceType.QRCode.name, "UH48557946", "10606",
                "2017/05/06", "Rkf8OrnSkKnUHt1ADac5nw==", "53664532", "4736")

        val queryInvoiceDetail3 = QueryInvoiceDetail(InvoiceType.QRCode.name, "VP08049571", "10608",
                "2017/07/19", "pRbdGP05A+5WhYk49OWudQ==", "80035874", "6585")

        val fields: Map<String, String> = SystemUtils.pojo2Map(queryInvoiceDetail)
        RetrofitClient.getEInvoiceAPI()
                .QueryInvoiceDetail(fields)
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

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listInvoiceAdapter
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    fun setFab() {
        fab.setOnClickListener {
            startActivity(Intent(this, QRcodeScannerActivity::class.java))
        }
    }
}

class ListInvoiceAdapter : RecyclerView.Adapter<ListInvoiceViewHolder>() {
    private var list: MutableList<QRCodeInvoice> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListInvoiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_list_invoice, parent, false)
//        view.setOnClickListener {
//            val intent = Intent(parent.context, InvoiceDetailActivity::class.java)
//            intent.putExtra(QRCodeInvoice.INVOICE_NUMBER, list[viewType].invNum)
//            parent.context.startActivity(intent)
//        }
        return ListInvoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListInvoiceViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun add(list: MutableList<QRCodeInvoice>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun update(list: MutableList<QRCodeInvoice>) {
        this.list.clear()
        this.list = list
        notifyDataSetChanged()
    }

}

class ListInvoiceViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
    private var invoice: QRCodeInvoice? = null

    constructor(view: View) : super(view) {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val intent = Intent(v.context, InvoiceDetailActivity::class.java)
        intent.putExtra(QRCodeInvoice.INVOICE_NUMBER, invoice?.invNum)
        v.context.startActivity(intent)
    }

    fun bind(invoice: QRCodeInvoice) {
        this.invoice = invoice
        with(itemView) {
            invNum.text = invoice.invNum
            invDate.text = DateUtils.dateFormat(invoice.invDate)
            sellerName.text = invoice.sellerName
            total.text = "${invoice.total}元"
            invSource.text = invoice.invoiceSource
        }
    }
}

