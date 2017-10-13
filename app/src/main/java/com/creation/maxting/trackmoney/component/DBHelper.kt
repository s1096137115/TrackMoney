package com.creation.maxting.trackmoney.component

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.creation.maxting.trackmoney.data.InvoiceDetail
import com.creation.maxting.trackmoney.data.QRCodeInvoice
import com.creation.maxting.trackmoney.data.ResponseInvoiceDetail
import com.creation.maxting.trackmoney.data.ScanInvoice

/**
 * Created by MAX on 2017/8/15.
 */
class DBHelper private constructor() : SQLiteOpenHelper(TMApplication.getContext(), "TMDatabase", null, _DB_VERSION) {
    companion object {
        private var instance: DBHelper? = null
        private val _DB_VERSION = 1

        internal fun getInstance(): DBHelper {
            if (instance == null) {
                instance = DBHelper()
            }
            return instance as DBHelper
        }

        fun clearInstance() {
            instance = null
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(QRCodeInvoice.CREATE_SQL)
        db.execSQL(InvoiceDetail.CREATE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
    }

    fun getInvoices(): MutableList<QRCodeInvoice> {
        val sql = "SELECT * FROM ${QRCodeInvoice.TABLE_NAME} ORDER BY ${QRCodeInvoice.INVOICE_DATE} DESC"
        val cursor: Cursor = writableDatabase.rawQuery(sql, null)
        val list: MutableList<QRCodeInvoice> = mutableListOf()
        while (cursor.moveToNext()) {
            list.add(QRCodeInvoice.newInstance(cursor))
        }
        cursor.close()
        return list
    }

    fun getInvoices(since: String, until: String): MutableList<QRCodeInvoice> {
        val sql = "SELECT * FROM ${QRCodeInvoice.TABLE_NAME} " +
                "WHERE ${QRCodeInvoice.INVOICE_DATE} >= $since " +
                "AND ${QRCodeInvoice.INVOICE_DATE} < $until " +
                "ORDER BY ${QRCodeInvoice.INVOICE_DATE}"
        val cursor: Cursor = writableDatabase.rawQuery(sql, null)
        val list: MutableList<QRCodeInvoice> = mutableListOf()
        while (cursor.moveToNext()) {
            list.add(QRCodeInvoice.newInstance(cursor))
        }
        cursor.close()
        return list
    }

    fun getInvoice(invNum: String): QRCodeInvoice? {
        val sql = "SELECT * FROM ${QRCodeInvoice.TABLE_NAME} " +
                "WHERE ${QRCodeInvoice.INVOICE_NUMBER} = '$invNum'"
        val cursor: Cursor = writableDatabase.rawQuery(sql, null)
        if(cursor.moveToNext()){
            return QRCodeInvoice.newInstance(cursor)
        }else{
            return null
        }
    }

    fun getInvoiceDetails(invNum: String): MutableList<InvoiceDetail> {
        val sql = "SELECT * FROM ${InvoiceDetail.TABLE_NAME} " +
                "WHERE  ${InvoiceDetail.INVOICE_NUMBER} = '$invNum' " +
                "ORDER BY ${InvoiceDetail.ROW_NUMBER}"
        val cursor: Cursor = writableDatabase.rawQuery(sql, null)
        val list: MutableList<InvoiceDetail> = mutableListOf()
        while (cursor.moveToNext()) {
            list.add(InvoiceDetail.newInstance(cursor))
        }
        cursor.close()
        return list
    }

    fun insertInvoice(invoice: QRCodeInvoice): Long {
        return writableDatabase.insert(QRCodeInvoice.TABLE_NAME, null, invoice.getContentValues())
    }

    fun insertInvoice(invoice: ScanInvoice): Long {
        return writableDatabase.insert(QRCodeInvoice.TABLE_NAME, null, invoice.getContentValues())
    }

    fun updateInvoice(invoice: QRCodeInvoice): Int {
        val whereSql = "${QRCodeInvoice.INVOICE_NUMBER} = '${invoice.invNum}'"
        return writableDatabase.update(QRCodeInvoice.TABLE_NAME, invoice.getContentValues(), whereSql, null)
    }

    fun updateInvoice(invoice: ResponseInvoiceDetail): Int {
        val whereSql = "${QRCodeInvoice.INVOICE_NUMBER} = '${invoice.invNum}'"
        return writableDatabase.update(QRCodeInvoice.TABLE_NAME, invoice.getContentValues(), whereSql, null)
    }

    fun deleteInvoice(invoice: QRCodeInvoice): Int {
        val whereSql = "${QRCodeInvoice.INVOICE_NUMBER} = '${invoice.invNum}'"
        return writableDatabase.delete(QRCodeInvoice.TABLE_NAME, whereSql, null)
    }

    fun insertInvoiceDetail(detail: InvoiceDetail) : Long {
        return writableDatabase.insert(InvoiceDetail.TABLE_NAME, null, detail.getContentValues())
    }

    fun deleteInvoiceDetails(detail: InvoiceDetail) : Int{
        val whereSql = "${InvoiceDetail.INVOICE_NUMBER} = '${detail.invNum}'"
        return writableDatabase.delete(InvoiceDetail.TABLE_NAME, whereSql, null)
    }

}