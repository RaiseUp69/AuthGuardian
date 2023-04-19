package com.authguardian.mobileapp.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class QrCodeGenerationViewModel(private val application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = SupervisorJob()

    // TODO: Why IO?
    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    private var ssid: String? = null
    private var password: String? = null

    // region LiveData

    private var _qrCode: MutableLiveData<Bitmap> = MutableLiveData()
    val qrCode: LiveData<Bitmap> = _qrCode

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading
    // endregion

    fun init(ssid: String?, password: String?) {
        this@QrCodeGenerationViewModel.ssid = ssid
        this@QrCodeGenerationViewModel.password = password

        if (ssid != null && password != null) {
            getQrCodeBitmap()
        }
    }

    private fun getQrCodeBitmap() {
        _isLoading.postValue(true)
        launch {
            val size = 512
            val qrCodeContent = "WIFI:S:$ssid;T:WPA;P:$password;;"
            val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 50 } // Make the QR code buffer border narrower
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

    companion object {

        private val TAG = QrCodeGenerationViewModel::class.java.simpleName
    }
}