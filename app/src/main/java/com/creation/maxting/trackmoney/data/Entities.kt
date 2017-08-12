package com.creation.maxting.trackmoney.data

/**
 * Created by MAX on 2017/8/3.
 */

//APP ID:EINV0201707333107
//API KEY:NEFzbmRuZzRVbnl3N2hFMg==

enum class InvoiceType {QRCode, Barcode }

enum class InvoiceAction {qryInvHeader, qryInvDetail }

enum class InvoiceAPIData(val value: String) {
    APPID("EINV0201707333107"),
    APIKey("NEFzbmRuZzRVbnl3N2hFMg==")
}

//https://einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp?version=0.3&type=Barcode&invNum=UJ65256975&action=qryInvHeader
//&generation=V2&invDate=2017/05/06&appID=EINV0201707333107&UUID=9f310b61-886d-4357-90d6-7eced2d34086

data class QueryInvoiceHeader(val version: Float, val type: String, val invNum: String,
                              val action: String = InvoiceAction.qryInvHeader.name,
                              val generation: String = "V2", val invDate: String, val UUID: String,
                              val appID: String = InvoiceAPIData.APPID.value)

data class ResponseInvoiceHeader(val v: String, val code: String, val msg: String, val invNum: String,
                                 val invDate: String, val sellerName: String, val invStatus: String,
                                 val invPeriod: String, val sellerBan: String, val sellerAddress: String?,
                                 val invoiceTime: String)

data class QueryInvoiceDetail(val version: Float, val type: String, val invNum: String,
                              val action: String = InvoiceAction.qryInvDetail.name,
                              val generation: String = "V2", val invTerm: String, val invDate: String,
                              val encrypt: String, val sellerID: String, val UUID: String,
                              val randomNumber: String, val appID: String = InvoiceAPIData.APPID.value)







