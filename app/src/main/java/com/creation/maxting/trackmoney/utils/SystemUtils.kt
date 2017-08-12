package com.creation.maxting.trackmoney.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*

/**
 * Created by MAX on 2017/8/4.
 */
object SystemUtils {
    val gson by lazy { GsonBuilder().create() }

    fun createUUID(): String = UUID.randomUUID().toString()

    fun getUUID(): String{
    var uuid: String? = PreferenceHelper.getUUID()
        if (uuid == null){
            uuid = createUUID()
            PreferenceHelper.setUUID(uuid)
        }
        return uuid
    }

    fun pojo2Map(obj: Any): Map<String, String> {
        val json: String = gson.toJson(obj)
        return gson.fromJson(json, Map::class.java) as  Map<String, String>
    }

}