package com.creation.maxting.trackmoney.data

import android.content.ContentValues
import android.database.Cursor
import com.creation.maxting.trackmoney.utils.DateUtils
import com.creation.maxting.trackmoney.utils.PreferenceHelper
import com.creation.maxting.trackmoney.utils.SystemUtils
import java.io.Serializable

/**
 * Created by MAX on 2017/8/3.
 */

//APP ID:EINV0201707333107
//API KEY:NEFzbmRuZzRVbnl3N2hFMg==

enum class InvoiceType {QRCode, Barcode }

enum class InvoiceAction {qryInvHeader, qryInvDetail, carrierInvChk, qryCarrierAgg }

enum class InvoiceAPIData(val value: String) {
    APPID("EINV0201707333107"),
    APIKey("NEFzbmRuZzRVbnl3N2hFMg==")
}

data class QRCodeInvoice(val invNum: String, val invDate: String, val randomNumber: String,
                         val salesAmount: Int, val total: Int, val buyerBan: String, val sellerBan: String,
                         val encrypt: String, val sellerName: String?, val sellerAddress: String?,
                         val invStatus: String?, val invPeriod: String?, val invoiceTime: String?,
                         val invoiceSource: String?) {
    var details: List<InvoiceDetail>? = null

    companion object {
        val TABLE_NAME = "QRCodeInvoice"

        //from scanInvoice
        val INVOICE_NUMBER = "invoiceNumber"
        val INVOICE_DATE = "invoiceDate"
        val RANDOM_NUMBER = "randomNumber"
        val SALES_AMOUNT = "salesAmount"
        val TOTAL = "total"
        val BUYER_BAN = "buyerBan"
        val SELLER_BAN = "sellerBan"
        val ENCRYPT = "encrypt"

        //from responseInvoice
        val SELLER_NAME = "sellerName"
        val SELLER_ADDRESS = "sellerAddress"
        val INVOICE_STATUS = "invoiceStatus"
        val INVOICE_PERIOD = "invoicePerild"
        val INVOICE_TIME = "invoiceTime"

        //from custom
        val INVOICE_SOURCE = "invoiceSoruce"

        val CREATE_SQL = "CREATE TABLE " +
                "$TABLE_NAME ( " +
                "$INVOICE_NUMBER VARCHAR(10) PRIMARY KEY, " +
                "$INVOICE_DATE VARCHAR(8), " +
                "$RANDOM_NUMBER VARCHAR(4), " +
                "$SALES_AMOUNT INTEGER, " +
                "$TOTAL INTEGER, " +
                "$BUYER_BAN VARCHAR(8), " +
                "$SELLER_BAN VARCHAR(8), " +
                "$ENCRYPT VARCHAR(24), " +
                "$SELLER_NAME VARCHAR(50), " +
                "$SELLER_ADDRESS VARCHAR(100), " +
                "$INVOICE_STATUS VARCHAR(20), " +
                "$INVOICE_PERIOD VARCHAR(8), " +
                "$INVOICE_TIME VARCHAR(8), " +
                "$INVOICE_SOURCE VARCHAR(8) " +
                ")"

        fun newInstance(cursor: Cursor): QRCodeInvoice {
            return QRCodeInvoice(
                    cursor.getString(cursor.getColumnIndex(INVOICE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(INVOICE_DATE)),
                    cursor.getString(cursor.getColumnIndex(RANDOM_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(SALES_AMOUNT)),
                    cursor.getInt(cursor.getColumnIndex(TOTAL)),
                    cursor.getString(cursor.getColumnIndex(BUYER_BAN)),
                    cursor.getString(cursor.getColumnIndex(SELLER_BAN)),
                    cursor.getString(cursor.getColumnIndex(ENCRYPT)),
                    cursor.getString(cursor.getColumnIndex(SELLER_NAME)),
                    cursor.getString(cursor.getColumnIndex(SELLER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(INVOICE_STATUS)),
                    cursor.getString(cursor.getColumnIndex(INVOICE_PERIOD)),
                    cursor.getString(cursor.getColumnIndex(INVOICE_TIME)),
                    cursor.getString(cursor.getColumnIndex(INVOICE_SOURCE))
            )
        }
    }

    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(INVOICE_NUMBER, invNum)
        values.put(INVOICE_DATE, invDate)
        values.put(RANDOM_NUMBER, randomNumber)
        values.put(SALES_AMOUNT, salesAmount)
        values.put(TOTAL, total)
        values.put(BUYER_BAN, buyerBan)
        values.put(SELLER_BAN, sellerBan)
        values.put(ENCRYPT, encrypt)
        return values
    }
}

//from scan
data class ScanInvoice(val invNum: String, val invDate: String, val randomNumber: String,
                       val salesAmount: Int, val total: Int, val buyerBan: String, val sellerBan: String,
                       val encrypt: String) : Serializable {

    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(QRCodeInvoice.INVOICE_NUMBER, invNum)
        values.put(QRCodeInvoice.INVOICE_DATE, DateUtils.convertTWDate(invDate, "yyyMMdd", "yyyyMMdd"))
        values.put(QRCodeInvoice.RANDOM_NUMBER, randomNumber)
        values.put(QRCodeInvoice.SALES_AMOUNT, salesAmount)
        values.put(QRCodeInvoice.TOTAL, total)
        values.put(QRCodeInvoice.BUYER_BAN, buyerBan)
        values.put(QRCodeInvoice.SELLER_BAN, sellerBan)
        values.put(QRCodeInvoice.ENCRYPT, encrypt)
        //custom
        values.put(QRCodeInvoice.INVOICE_SOURCE, "手動")
        return values
    }
}

data class QueryInvoiceHeader(val type: String, val invNum: String, val invDate: String) {
    val version: String = "0.3"
    val action: String = InvoiceAction.qryInvHeader.name
    val generation: String = "V2"
    val appID: String = InvoiceAPIData.APPID.value
    val UUID: String = SystemUtils.getUUID()
}

data class ResponseInvoiceHeader(val v: String, val code: String, val msg: String, val invNum: String,
                                 val invDate: String, val sellerName: String, val invStatus: String,
                                 val invPeriod: String, val sellerBan: String, val sellerAddress: String?,
                                 val invoiceTime: String)

data class QueryInvoiceDetail(val type: String, val invNum: String, val invTerm: String, val invDate: String,
                              val encrypt: String, val sellerID: String, val randomNumber: String) {
    val version: String = "0.3"
    val action: String = InvoiceAction.qryInvDetail.name
    val generation: String = "V2"
    val appID: String = InvoiceAPIData.APPID.value
    val UUID: String = SystemUtils.getUUID()
}

data class ResponseInvoiceDetail(val v: String, val code: String, val msg: String, val invNum: String,
                                 val invDate: String, val sellerName: String, val invStatus: String,
                                 val invPeriod: String, val sellerBan: String, val sellerAddress: String?,
                                 val invoiceTime: String, val details: List<InvoiceDetail>) {

    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(QRCodeInvoice.SELLER_NAME, sellerName)
        values.put(QRCodeInvoice.SELLER_ADDRESS, sellerAddress)
        values.put(QRCodeInvoice.INVOICE_STATUS, invStatus)
        values.put(QRCodeInvoice.INVOICE_PERIOD, invPeriod)
        values.put(QRCodeInvoice.INVOICE_TIME, invoiceTime)
        return values
    }
}

data class InvoiceDetail(val rowNum: String, val description: String, val quantity: String,
                         val unitPrice: String, var amount: String, var invNum: String?) {
    companion object {
        val TABLE_NAME = "InvoiceDetail"

        val ID = "_id"
        val INVOICE_NUMBER = QRCodeInvoice.INVOICE_NUMBER
        //from api
        val ROW_NUMBER = "rowNum"
        val DESCRIPTION = "description"
        val QUANTITY = "quantity"
        val UNIT_PRICE = "unitPrice"
        val AMOUNT = "amount"

        val CREATE_SQL = "CREATE TABLE " +
                "${InvoiceDetail.TABLE_NAME} ( " +
                "${InvoiceDetail.ID} INTEGER AUTO_INCREMENT, " +
                "${InvoiceDetail.ROW_NUMBER} VARCHAR(10), " +
                "${InvoiceDetail.DESCRIPTION} VARCHAR(50), " +
                "${InvoiceDetail.QUANTITY} VARCHAR(10), " +
                "${InvoiceDetail.UNIT_PRICE} VARCHAR(10), " +
                "${InvoiceDetail.AMOUNT} VARCHAR(10), " +
                "${InvoiceDetail.INVOICE_NUMBER} VARCHAR(10) " +
                ")"

        fun newInstance(cursor: Cursor): InvoiceDetail {
            return InvoiceDetail(
                    cursor.getString(cursor.getColumnIndex(InvoiceDetail.ROW_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(InvoiceDetail.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(InvoiceDetail.QUANTITY)),
                    cursor.getString(cursor.getColumnIndex(InvoiceDetail.UNIT_PRICE)),
                    cursor.getString(cursor.getColumnIndex(InvoiceDetail.AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(InvoiceDetail.INVOICE_NUMBER))
            )
        }
    }

    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(InvoiceDetail.ROW_NUMBER, rowNum)
        values.put(InvoiceDetail.DESCRIPTION, description)
        values.put(InvoiceDetail.QUANTITY, quantity)
        values.put(InvoiceDetail.UNIT_PRICE, unitPrice)
        values.put(InvoiceDetail.AMOUNT, amount)
        values.put(InvoiceDetail.INVOICE_NUMBER, invNum)
        return values
    }
}

//CardType
//3J0002 手機條碼
//1K0001 悠遊卡
//2G0001 iCash

data class CarrierInvoiceCheck(){
    val version: String = "0.3"
    val action: String = InvoiceAction.carrierInvChk.name
    val timeStamp: String = (System.currentTimeMillis() + 60000L).toString().substring(0, 10)
    val cardType: String = "3J0002"
    val cardNo: String? = PreferenceHelper.getCardNo()
    val cardEncrypt: String? = PreferenceHelper.getCardEncrypt()
}

data class CarrierInvoiceHeader(val version: String)

data class QueryCarrierAggregate(val serial: String, val cardNo: String,
                                 val cardEncrypt: String) {
    val version: String = "1.0"
    val action: String = InvoiceAction.qryCarrierAgg.name
    val cardType: String = "3J0002"
    val appID: String = InvoiceAPIData.APPID.value
    val uuid: String = SystemUtils.getUUID()
    val timeStamp: String = (System.currentTimeMillis() + 60000L).toString().substring(0, 10)
    val signature: String = SystemUtils.getHmacSHA1(InvoiceAPIData.APIKey.value,
            "action=${action}&appID=${appID}&cardEncrypt=${cardEncrypt}&cardNo=${cardNo}" +
                    "&cardType=${cardType}&serial=${serial}&timeStamp=${timeStamp}&uuid=${uuid}&version=${version}")
}

data class ResponseCarrierAggregate(val v: String, val code: String, val hashSerial: String, val msg: String,
                                    val cardType: String, val cardNo: String, val carriers: List<Carrier>)

data class Carrier(val carrierType: String, val carrierId2: String, val carrierName: String)








