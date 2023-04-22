package com.authguardian.mobileapp.utils

import android.os.Build

object UniversalUtils {

    fun getDeviceBrandAndModel(): String = "${Build.BRAND} ${Build.MODEL}"
}