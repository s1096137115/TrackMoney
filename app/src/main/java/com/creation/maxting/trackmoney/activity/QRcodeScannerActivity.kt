package com.creation.maxting.trackmoney.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.creation.maxting.trackmoney.data.*
import com.creation.maxting.trackmoney.utils.DateUtils
import com.creation.maxting.trackmoney.utils.RetrofitClient
import com.creation.maxting.trackmoney.utils.SystemUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView


/**
 * Created by MAX on 2017/8/6.
 */
class QRcodeScannerActivity : BaseActivity(), ZBarScannerView.ResultHandler {
    private val REQUEST_CAMERA = 1
    private val mScannerView by lazy {
        ZBarScannerView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mScannerView = ZBarScannerView(this)
        setContentView(mScannerView)
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    fun checkPermission() {
        val permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA ->
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mScannerView.startCamera()
                }else{
                    finish()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        println("<<<<<<<<<<<<result:${result.contents}")
        val codeInfo: List<String> = result.contents.split(":")
        if (codeInfo[0].length == 77) {
            val invoice: ScanInvoice = packageInvoice(codeInfo[0])
            val value = mDB.insertInvoice(invoice)
            if (value != -1L) {
                apiDetail(invoice)
            } else {
                Toast.makeText(this, "此發票已經存在", Toast.LENGTH_SHORT).show()
                mScannerView.resumeCameraPreview(this)
            }
        } else {
            mScannerView.resumeCameraPreview(this)
        }
    }

    fun packageInvoice(data: String): ScanInvoice {
        with(data) {
            return ScanInvoice(substring(0, 10), substring(10, 17), substring(17, 21),
                    Integer.parseInt(substring(21, 29), 16),
                    Integer.parseInt(substring(29, 37), 16),
                    substring(37, 45), substring(45, 53), substring(53, 77))
        }
    }

    private fun apiDetail(invoice: ScanInvoice) {
        val queryInvoiceDetail = QueryInvoiceDetail(InvoiceType.QRCode.name, invoice.invNum,
                DateUtils.getTerm(invoice.invDate),
                DateUtils.convertTWDate(invoice.invDate, "yyyMMdd", "yyyy/MM/dd")!!,
                invoice.encrypt, invoice.sellerBan, invoice.randomNumber)

        val queryInvoiceDetail2 = QueryInvoiceDetail(InvoiceType.QRCode.name, "UJ65276333", "10606",
                "2017/06/29", "5MaIynpBIR9c03JiHSJfLg==", "53658095", "4750")

        val fields: Map<String, String> = SystemUtils.pojo2Map(queryInvoiceDetail)
        RetrofitClient.getEInvoiceAPI()
                .QueryInvoiceDetail(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            repsonseInvoiceDetail ->
                            saveDB(repsonseInvoiceDetail)
                            val intent = Intent(this, InvoiceDetailActivity::class.java)
                            intent.putExtra(QRCodeInvoice.INVOICE_NUMBER, invoice.invNum)
                            startActivity(intent)
                            finish()
                        },
                        {
                            error ->
                            Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                        })
    }

    fun saveDB(response: ResponseInvoiceDetail) {
        mDB.updateInvoice(response)
        for (detail in response.details) {
            detail.invNum = response.invNum
            //消除小數點 double -> int -> string
            detail.amount = detail.amount.toDouble().toInt().toString()
            val b = mDB.insertInvoiceDetail(detail)
            println("$b")
        }
    }


}