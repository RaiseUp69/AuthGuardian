package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent

class AuthMethodViewModel : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.AUTH_METHOD_SCRN__VIEW.value)
    }
}
