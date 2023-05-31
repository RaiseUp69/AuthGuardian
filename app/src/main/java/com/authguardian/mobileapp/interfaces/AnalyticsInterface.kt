package com.authguardian.mobileapp.interfaces

interface AnalyticsInterface {
    fun sendEvent(event: String, params: Map<String, Any> = emptyMap()) {}
}