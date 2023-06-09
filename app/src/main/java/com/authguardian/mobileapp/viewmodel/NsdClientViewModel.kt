package com.authguardian.mobileapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.enums.AnalyticsEventAction
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.utils.AnalyticsUtils.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private val _receivedMessage: MutableLiveData<String> = MutableLiveData()
    val receivedMessage: LiveData<String> = _receivedMessage

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading
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
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000) // only for testing
            try {
                val socket = Socket(serviceAddress, servicePort)

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = PrintWriter(socket.getOutputStream(), true)

                output.println("Hello from client!")

                val receivedData = input.readLine()
                withContext(Dispatchers.Main) {
                    _receivedMessage.postValue(receivedData)
                    _isLoading.postValue(false)
                }
                Log.d("NSD", "Received data from server: $receivedData")

                socket.close()
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    _isLoading.postValue(false)
                }
                Log.e("NSD", "Client socket error: ", e)
            }
        }
    }

    fun onBackPressed() {
        sendEvent(AnalyticsEventAction.NSD_CLIENT_BTN_BACK_TAP.value)
    }
}