package com.creation.maxting.trackmoney.component

import android.app.Application
import android.content.Context

/**
 * Created by MAX on 2017/8/11.
 */
class TMApplication : Application() {

    companion object {
        private lateinit var mContext: Context

        @JvmStatic
        fun getContext(): Context = mContext
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

}