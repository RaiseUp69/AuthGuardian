package com.authguardian.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.enums.AnalyticsEventAction
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

class NsdClientViewModel : ViewModel() {

    // region Data

    private var isInited: Boolean = false
    // endregion

    // region LiveData

    private val _receivedMessage = MutableStateFlow("")
    val receivedMessage = _receivedMessage.asStateFlow()

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()
    // endregion

    fun init(): Boolean = when {
        isInited -> true
        else -> {
            sendEvent(AnalyticsEventScreen.NSD_CLIENT_SCRN__VIEW.value)
            isInited = true
            true
        }
    }

    fun startClientSocket(serviceAddress: InetAddress, servicePort: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            delay(1000) // only for testing
            try {
                val socket = Socket(serviceAddress, servicePort)

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = PrintWriter(socket.getOutputStream(), true)

                output.println("Hello from client!")

                val receivedData = input.readLine()
                withContext(Dispatchers.Main) {
                    _receivedMessage.value = receivedData
                    _isLoading.emit(false)
                }
                Log.d("NSD", "Received data from server: $receivedData")

                socket.close()
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    _isLoading.emit(false)
                }
                Log.e("NSD", "Client socket error: ", e)
            }
        }
    }

    fun onBackPressed() {
        sendEvent(AnalyticsEventAction.NSD_CLIENT_BTN_BACK_TAP.value)
    }
}