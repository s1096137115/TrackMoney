package com.creation.maxting.trackmoney.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.creation.maxting.trackmoney.component.DBHelper

/**
 * Created by MAX on 2017/8/2.
 */

abstract class BaseActivity : AppCompatActivity(){
    protected lateinit var mDB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDB = DBHelper.getInstance()
    }
}
