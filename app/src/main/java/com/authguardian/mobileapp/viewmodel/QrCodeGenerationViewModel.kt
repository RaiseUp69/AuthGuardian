package com.authguardian.mobileapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.interfaces.AnalyticsInterface
import com.authguardian.mobileapp.repository.DataStoreRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QrCodeGenerationViewModel(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
    private val analyticsUtils: AnalyticsInterface
) : AndroidViewModel(application) {

    // region LiveData

    private var _qrCode = MutableStateFlow<Bitmap>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))
    val qrCode = _qrCode.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _savedMessage = MutableStateFlow("")
    val savedMessage = _savedMessage.asStateFlow()
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
            analyticsUtils.sendEvent(AnalyticsEventScreen.QR_CODE_GENERATION_SCRN__VIEW.value)
            if (ssid != null && password != null) {
                generateQrCode()
            }

            isInited = true
            true
        }
    }

    private fun generateQrCode() {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            val size = 512
            val qrCodeContent = "WIFI:S:${dataStoreRepository.getString(USER_SSID) ?: ssid};T:WPA;P:${dataStoreRepository.getString(USER_PASSWORD) ?: password};;"
            val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
            _qrCode.value = (Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
                for (x in 0 until size) {
                    for (y in 0 until size) {
                        it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            })
            _isLoading.value = false
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
                _savedMessage.value = getApplication<Application>().getString(R.string.qr_code_saved)
            }
        }
    }

    companion object {

        const val USER_SSID = "USER_SSID"
        const val USER_PASSWORD = "USER_PASSWORD"
    }
}