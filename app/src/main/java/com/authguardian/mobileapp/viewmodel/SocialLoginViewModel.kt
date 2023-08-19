package com.authguardian.mobileapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.authguardian.mobileapp.enums.AnalyticsEventAction
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SocialLoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _isGoogleSignInAvailable = MutableStateFlow(false)
    val isGoogleSignInAvailable = _isGoogleSignInAvailable.asStateFlow()

    fun init() {
        sendEvent(AnalyticsEventScreen.SOCIAL_LOGIN_SCRN__VIEW.value)
    }

    fun isSignInAvailable(context: Context) {
        /**
        Check for existing Google account, if the user is already signed in
        account will be non-null.
         */
        val account = GoogleSignIn.getLastSignedInAccount(context)
        _isGoogleSignInAvailable.value = account == null
    }

    fun onGoogleSignInClicked() {
        sendEvent(AnalyticsEventAction.GOOGLE_SIGN_IN_BTN_TAP.value)
    }

    fun onGoogleSignOutClicked() {
        sendEvent(AnalyticsEventAction.GOOGLE_SIGN_OUT_BTN_TAP.value)
    }
}