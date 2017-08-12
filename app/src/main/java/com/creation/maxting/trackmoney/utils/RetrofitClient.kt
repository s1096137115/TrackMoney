package com.creation.maxting.trackmoney.utils

import com.creation.maxting.trackmoney.data.InvoiceAPI
import com.creation.maxting.trackmoney.data.QueryInvoiceHeader
import com.creation.maxting.trackmoney.data.ResponseInvoiceHeader
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap

/**
 * Created by MAX on 2017/8/4.
 */
object RetrofitClient {
    private val retrofit: Retrofit = Retrofit
            .Builder()
            .baseUrl("https://einvoice.nat.gov.tw/PB2CAPIVAN/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun getEInvoiceAPI(fields: Map<String, String>) : Single<ResponseInvoiceHeader> {
        return retrofit.create(InvoiceAPI::class.java).QueryInvoiceHeader(fields)
    }

}