package com.creation.maxting.trackmoney.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.creation.maxting.trackmoney.component.DBHelper

/**
 * Created by MAX on 2017/8/3.
 */
abstract class BaseFragment: Fragment(){
    protected lateinit var mDB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDB = DBHelper.getInstance()
    }
}