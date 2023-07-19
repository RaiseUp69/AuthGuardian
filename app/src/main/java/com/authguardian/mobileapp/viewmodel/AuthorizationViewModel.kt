package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.interfaces.AnalyticsInterface

class AuthorizationViewModel(private val analyticsUtils: AnalyticsInterface) : ViewModel() {

    fun init() {
        analyticsUtils.sendEvent(AnalyticsEventScreen.QR_AUTHORIZATION_SCRN__VIEW.value)
    }
}