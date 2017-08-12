package com.creation.maxting.trackmoney.data

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by MAX on 2017/8/4.
 */
interface InvoiceAPI {
    @FormUrlEncoded
    @POST("invapp/InvApp")
    fun QueryInvoiceHeader(@FieldMap fields: Map<String, String>): Single<ResponseInvoiceHeader>
}
