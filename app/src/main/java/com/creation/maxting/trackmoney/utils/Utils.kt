package com.creation.maxting.trackmoney.utils

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.*
import java.text.SimpleDateFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import android.R.id.message




/**
 * Created by MAX on 2017/8/4.
 */
object SystemUtils {
    val gson by lazy { GsonBuilder().create() }

    fun createUUID(): String = UUID.randomUUID().toString()

    fun getUUID(): String {
        var uuid: String? = PreferenceHelper.getUUID()
        if (uuid == null) {
            uuid = createUUID()
            PreferenceHelper.setUUID(uuid)
        }
        return uuid
    }

    fun pojo2Map(obj: Any): Map<String, String> {
        val json: String = gson.toJson(obj)
        return gson.fromJson(json, Map::class.java) as Map<String, String>
    }

    @Throws(Exception::class)
    fun getHmacSHA1(privateKey: String, input: String): String {
        val algorithm = "HmacSHA1"
        val key = SecretKeySpec(privateKey.toByteArray(), algorithm)
        val mac = Mac.getInstance(algorithm)
        mac.init(key)
        return Base64.encodeToString(mac.doFinal(input.toByteArray()), Base64.NO_WRAP)
    }

}

object DateUtils {
    private val calender: Calendar = Calendar.getInstance()

    fun getThisYear(): Int = calender.get(Calendar.YEAR)

    fun getThisMonth(): Int = calender.get(Calendar.MONTH) + 1

    fun getThisDay(): Int = calender.get(Calendar.DAY_OF_MONTH)

    /**
     *可民國西元互轉
     * convertTWDate("2013/05/08","yyyy/MM/dd","yyyMMdd")
     * convertTWDate("2013/05/08","yyyy/MM/dd","yyy/MM/dd")
     * convertTWDate("2013/05/08","yyyy/MM/dd","yyy-MM-dd")
     * @return
     */
    fun convertTWDate(AD: String?, beforeFormat: String, afterFormat: String): String? {//轉年月格式
        if (AD == null) return ""
        val df4 = SimpleDateFormat(beforeFormat)
        val df2 = SimpleDateFormat(afterFormat)
        val cal = Calendar.getInstance()
        try {
            cal.time = df4.parse(AD)
            if (cal.get(Calendar.YEAR) > 1492)
                cal.add(Calendar.YEAR, -1911)
            else
                cal.add(Calendar.YEAR, +1911)
            return df2.format(cal.time)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     *取得發票期別
     * @return
     */
    fun getTerm(date: String): String { //yyyMMdd
//        val year = date.subSequence(0, 3)
//        val month = date.substring(3, 5)
        var value = date.substring(0, 5).toInt()
        if (value % 2 != 0) {
            value += 1
        }
        return value.toString()
    }

    fun getPeriod(term: String): String { //yyyMM
        val year = term.subSequence(0, 3)
        val until = term.substring(3, 5)
        val since = DecimalFormat("00").format(until.toInt() - 1)
        return "${year}年${since}-${until}月"
    }

    fun dateFormat(date: String): String {//yyyyMMdd ->yyyy/MM/dd
        val dateType = SimpleDateFormat("yyyyMMdd", Locale.TAIWAN).parse(date)
        return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(dateType)
    }

}