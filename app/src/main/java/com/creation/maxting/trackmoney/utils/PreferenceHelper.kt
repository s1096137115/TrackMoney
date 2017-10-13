package com.creation.maxting.trackmoney.utils

import android.app.Activity
import com.creation.maxting.trackmoney.component.TMApplication

/**
 * Created by MAX on 2017/8/11.
 */
object PreferenceHelper {
    private val APP_DATA_PREFERENCE = "appData"
    private val PREFERENCES_FIELD_APP_UUID = "UUID"

    fun getUUID(): String? {
        return TMApplication.getContext()
                .getSharedPreferences(APP_DATA_PREFERENCE, Activity.MODE_PRIVATE)
                .getString(PREFERENCES_FIELD_APP_UUID, null)
    }

    fun setUUID(UUID: String) {
        TMApplication.getContext()
                .getSharedPreferences(APP_DATA_PREFERENCE, Activity.MODE_PRIVATE)
                .edit()
                .putString(PREFERENCES_FIELD_APP_UUID, UUID)
                .apply()
    }

    private val USER_DATA_PREFERENCE = "userData"
    private val PREFERENCES_FIELD_USER_CARD_NO = "cardNo"
    private val PREFERENCES_FIELD_USER_CARD_ENCRYPT = "cardEncrypt"

    fun getCardNo(): String? {
        return TMApplication.getContext()
                .getSharedPreferences(USER_DATA_PREFERENCE, Activity.MODE_PRIVATE)
                .getString(PREFERENCES_FIELD_USER_CARD_NO, null)
    }

    fun setCardNo(cardNo: String) {
        TMApplication.getContext()
                .getSharedPreferences(USER_DATA_PREFERENCE, Activity.MODE_PRIVATE)
                .edit()
                .putString(PREFERENCES_FIELD_USER_CARD_NO, cardNo)
                .apply()
    }

    fun getCardEncrypt(): String? {
        return TMApplication.getContext()
                .getSharedPreferences(USER_DATA_PREFERENCE, Activity.MODE_PRIVATE)
                .getString(PREFERENCES_FIELD_USER_CARD_ENCRYPT, null)
    }

    fun setCardEncrypt(cardEncrypt: String) {
        TMApplication.getContext()
                .getSharedPreferences(USER_DATA_PREFERENCE, Activity.MODE_PRIVATE)
                .edit()
                .putString(PREFERENCES_FIELD_USER_CARD_ENCRYPT, cardEncrypt)
                .apply()
    }
}