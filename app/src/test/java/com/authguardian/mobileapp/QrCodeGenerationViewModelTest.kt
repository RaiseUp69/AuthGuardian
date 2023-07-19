package com.authguardian.mobileapp

import android.app.Application
import com.authguardian.mobileapp.interfaces.AnalyticsInterface
import com.authguardian.mobileapp.repository.DataStoreRepository
import com.authguardian.mobileapp.viewmodel.QrCodeGenerationViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class QrCodeGenerationViewModelTest {

    private val dataStoreRepository: DataStoreRepository = mockk(relaxed = true)
    private val analyticsUtils: AnalyticsInterface = mockk(relaxed = true)
    private val application: Application = mockk(relaxed = true)

    private val viewModel = QrCodeGenerationViewModel(application, dataStoreRepository, analyticsUtils)

    @Test
    fun `Given null ssid, when init is called, QR code shouldn't be generated`() {
        // Given
        val ssid = null

        // When
        viewModel.init(ssid, "1234")

        // Then
        assertNull(viewModel.qrCode.value)
        assertFalse(viewModel.isLoading.value!!)
    }

    @Test
    fun `Given null password, when init is called, QR code shouldn't be generated`() {
        // Given
        val password = null

        // When
        viewModel.init("ssid", password = password)

        // Then
        assertNull(viewModel.qrCode.value)
        assertFalse(viewModel.isLoading.value!!)
    }
}