package com.authguardian.mobileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.repository.MessageRepository
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import kotlinx.coroutines.launch

class GoogleSignInSuccessfulViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    fun init() {
        sendEvent(AnalyticsEventScreen.GOOGLE_SIGN_SUCCESSFUL_SCRN__VIEW.value)
        viewModelScope.launch {
            try {
                messageRepository.postMessage("Google sign in successful")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}