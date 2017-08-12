package com.creation.maxting.trackmoney.utils

import android.app.Activity
import android.app.Application
import com.creation.maxting.trackmoney.component.TMApplication

/**
 * Created by MAX on 2017/8/11.
 */
object PreferenceHelper {
    private val APP_STATUS_PREFERENCE = "appStatus"
    private val PREFERENCES_FIELD_APP_UUID = "UUID"

    fun getUUID(): String? {
        return TMApplication.getContext()
                .getSharedPreferences(APP_STATUS_PREFERENCE, Activity.MODE_PRIVATE)
                .getString(PREFERENCES_FIELD_APP_UUID, null)
    }

    fun setUUID(UUID: String){
        TMApplication.getContext()
                .getSharedPreferences(APP_STATUS_PREFERENCE, Activity.MODE_PRIVATE)
                .edit()
                .putString(PREFERENCES_FIELD_APP_UUID, UUID)
                .apply()
    }
}