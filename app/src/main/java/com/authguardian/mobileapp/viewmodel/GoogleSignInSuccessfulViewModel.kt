package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent

class GoogleSignInSuccessfulViewModel : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.GOOGLE_SIGN_SUCCESSFUL_SCRN__VIEW.value)
    }
}