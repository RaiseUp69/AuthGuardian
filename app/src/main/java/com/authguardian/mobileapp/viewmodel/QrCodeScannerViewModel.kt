package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent

class QrCodeScannerViewModel : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.QR_CODE_SCANNER_SCRN__VIEW.value)
    }

    companion object {

        val TAG: String = QrCodeScannerViewModel::class.java.simpleName
    }
}