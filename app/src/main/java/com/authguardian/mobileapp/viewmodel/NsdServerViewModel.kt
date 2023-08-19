package com.authguardian.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private var bufferedReader: BufferedReader? = null
    private var printWriter: PrintWriter? = null
    // endregion

    // region LiveData

    private val _receivedMessage = MutableStateFlow("")
    val receivedMessage = _receivedMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    // endregion

    fun init(servicePort: Int): Boolean = when {
        isInited -> true
        else -> {
            this.servicePort = servicePort
            sendEvent(AnalyticsEventScreen.NSD_SERVER_SCRN__VIEW.value)
            isInited = true
            true
        }
    }

    fun startServerSocket() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val serverSocket = ServerSocket(servicePort!!)
                val socket = serverSocket.accept()

                bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                printWriter = PrintWriter(socket.getOutputStream(), true)

                printWriter?.println("Hello from server!")
                val receivedData = bufferedReader?.readLine()
                withContext(Dispatchers.Main) {
                    _receivedMessage.value = receivedData.toString()
                    _isLoading.value = false
                }
                socket.close()
                serverSocket.close()
            } catch (e: IOException) {
                _isLoading.value = false
                Log.e("NSD", "Server socket error: ", e)
            }
            _isLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        bufferedReader?.close()
        printWriter?.close()
        viewModelScope.cancel()
    }
}