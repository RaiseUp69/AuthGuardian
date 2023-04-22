package com.authguardian.mobileapp.utils

import android.content.Context

class NsdServiceUtils(private val context: Context) {

    companion object {
        // From work notebook
        val TAG: String = NsdServiceUtils::class.java.simpleName
        const val NSD_AUTH = "NSD_AUTH"
        const val NSD_SERVICE_TYPE = "_nsdchat._tcp"
    }
}