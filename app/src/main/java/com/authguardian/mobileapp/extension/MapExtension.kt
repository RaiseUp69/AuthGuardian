package com.authguardian.mobileapp.extension

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun Map<String, Any?>.toBundle(): Bundle = Bundle().apply {
    forEach { (key, value) ->
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Byte -> putByte(key, value)
            is Char -> putChar(key, value)
            is Short -> putShort(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is String -> putString(key, value)
            is CharSequence -> putCharSequence(key, value)
            is Parcelable -> putParcelable(key, value)
            is Serializable -> putSerializable(key, value)
        }
    }
}