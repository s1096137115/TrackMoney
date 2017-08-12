package com.creation.maxting.trackmoney.activity

import android.os.Bundle
import android.widget.Toast
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

/**
 * Created by MAX on 2017/8/6.
 */
class QRcodeScannerActivity : BaseActivity(), ZBarScannerView.ResultHandler {
//    private lateinit var mScannerView: ZBarScannerView
    private val mScannerView by lazy {
    ZBarScannerView(this)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mScannerView = ZBarScannerView(this)
        setContentView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.setAutoFocus(false)
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        Toast.makeText(this, result.contents, Toast.LENGTH_SHORT).show()
        mScannerView.resumeCameraPreview(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}