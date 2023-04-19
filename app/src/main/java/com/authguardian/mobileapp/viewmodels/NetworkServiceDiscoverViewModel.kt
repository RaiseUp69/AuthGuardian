package com.authguardian.mobileapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel

class NetworkServiceDiscoverViewModel(private val application: Application) : AndroidViewModel(application) {

    fun init() {
        Toast.makeText(application, "Service is started", Toast.LENGTH_SHORT).show()
    }
}