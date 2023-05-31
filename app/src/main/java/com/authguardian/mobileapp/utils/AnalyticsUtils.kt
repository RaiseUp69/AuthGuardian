package com.authguardian.mobileapp.utils

import com.authguardian.mobileapp.extension.toBundle
import com.authguardian.mobileapp.interfaces.AnalyticsInterface
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsUtils : AnalyticsInterface {

    private val firebaseAnalytics = Firebase.analytics

    override fun sendEvent(event: String, params: Map<String, Any>) {
        firebaseAnalytics.logEvent(event, params.toBundle())
    }
}