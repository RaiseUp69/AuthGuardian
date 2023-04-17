package com.authguardian.mobileapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel

class AuthenticationViewModel(private val application: Application) : AndroidViewModel(application) {

    fun init() {
        Toast.makeText(application, "VIEW_MODEL_IS_INITIALIZED", Toast.LENGTH_SHORT).show()
    }
}