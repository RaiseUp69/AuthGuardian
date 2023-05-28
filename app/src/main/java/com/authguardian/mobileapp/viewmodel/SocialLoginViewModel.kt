package com.authguardian.mobileapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SocialLoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _isGoogleSignInAvailable = MutableLiveData<Boolean>()
    val isGoogleSignInAvailable: LiveData<Boolean> = _isGoogleSignInAvailable

    fun init() {

    }

    fun checkIfUserAlreadySignedIn(context: Context) {
        /*
            Check for existing Google Sign In account, if the user is already signed in
            the GoogleSignInAccount will be non-null.
        */
        val account = GoogleSignIn.getLastSignedInAccount(context)
        _isGoogleSignInAvailable.value = account == null
    }
}