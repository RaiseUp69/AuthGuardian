package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent

class NsdChooseRoleViewModel : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.NSD_CHOOSE_ROLE_SCRN__VIEW.value)
    }
}