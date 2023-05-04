package com.authguardian.mobileapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.repository.DataStoreRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QrCodeGenerationViewModel(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    // region LiveData

    private var _qrCode: MutableLiveData<Bitmap> = MutableLiveData()
    val qrCode: LiveData<Bitmap> = _qrCode

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _savedMessage: MutableLiveData<String> = MutableLiveData()
    val savedMessage: LiveData<String> = _savedMessage
    // endregion

    // region Data

    private var ssid: String? = null
    private var password: String? = null
    private var isInited: Boolean = false
    // endregion

    fun init(ssid: String?, password: String?): Boolean = when {
        isInited -> true
        else -> {
            this@QrCodeGenerationViewModel.ssid = ssid
            this@QrCodeGenerationViewModel.password = password

            if (ssid != null && password != null) {
                generateQrCode()
            }

            isInited = true
            true
        }
    }

    private fun generateQrCode() {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.postValue(true)
            delay(1000) // only for demonstration
            val size = 512
            val qrCodeContent = "WIFI:S:${dataStoreRepository.getString(USER_SSID) ?: ssid};T:WPA;P:${dataStoreRepository.getString(USER_PASSWORD) ?: password};;"
            val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
            _qrCode.postValue(Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
                for (x in 0 until size) {
                    for (y in 0 until size) {
                        it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            })
            _isLoading.postValue(false)
        }
    }

    fun saveUserData() {
        // TODO: Add the ability to clean up qr code from datastore
        viewModelScope.launch {
            if (dataStoreRepository.getString(USER_SSID) == null && dataStoreRepository.getString(USER_PASSWORD) == null) {
                dataStoreRepository.apply {
                    putString(USER_SSID, ssid!!)
                    putString(USER_PASSWORD, password!!)
                }
                _savedMessage.postValue(getApplication<Application>().getString(R.string.qr_code_saved))
            }
        }
    }

    companion object {

        private const val USER_SSID = "USER_SSID"
        private const val USER_PASSWORD = "USER_PASSWORD"
        private val TAG = QrCodeGenerationViewModel::class.java.simpleName
    }
}