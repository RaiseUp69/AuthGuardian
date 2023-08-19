package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.repository.MessageRepository
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthMethodViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.AUTH_METHOD_SCRN__VIEW.value)
    }

    fun postMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                messageRepository.postMessage(text)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
