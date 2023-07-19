package com.authguardian.mobileapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.module.NetworkModule
import com.authguardian.mobileapp.request.MessageRequest
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import com.authguardian.mobileapp.utils.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthMethodViewModel : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.AUTH_METHOD_SCRN__VIEW.value)
    }

    fun postMessage(context: Context, message: String) {
        val okHttpInstance = NetworkModule().getOkHttpClientInstance(context)
        val messageService = Network.getMessageServiceInstance(Network.getRetrofitInstance(okHttpInstance))

        viewModelScope.launch(Dispatchers.IO) {
            try {
                messageService.postMessage(MessageRequest(message))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
