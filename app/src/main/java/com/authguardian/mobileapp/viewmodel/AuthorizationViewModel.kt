package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent

class AuthorizationViewModel : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.QR_AUTHORIZATION_SCRN__VIEW.value)
    }
}