package com.authguardian.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket


class NsdServerViewModel : ViewModel() {

    // region Data

    private var isInited: Boolean = false
    private var servicePort: Int? = null
    // endregion

    // region LiveData

    private val _receivedMessage: MutableLiveData<String> = MutableLiveData()
    val receivedMessage: LiveData<String> = _receivedMessage

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading
    // endregion

    fun init(servicePort: Int): Boolean = when {
        isInited -> true
        else -> {
            this.servicePort = servicePort

            isInited = true
            true
        }
    }

    fun startServerSocket() {
        _isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                delay(1000) // only for testing
                try {
                    val serverSocket = ServerSocket(servicePort!!)
                    val socket = serverSocket.accept()

                    val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val output = PrintWriter(socket.getOutputStream(), true)

                    output.println("Hello from server!")
                    val receivedData = input.readLine()
                    withContext(Dispatchers.Main) {
                        _receivedMessage.postValue(receivedData)
                        _isLoading.postValue(false)
                    }
                    socket.close()
                    serverSocket.close()
                } catch (e: IOException) {
                    _isLoading.postValue(false)
                    Log.e("NSD", "Server socket error: ", e)
                }
            }
        }
    }
}