package com.authguardian.mobileapp

import com.authguardian.mobileapp.enums.AnalyticsEventScreen
import com.authguardian.mobileapp.interfaces.AnalyticsInterface
import com.authguardian.mobileapp.viewmodel.AuthorizationViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class AuthorizationViewModelTest {

    private val analyticsUtils: AnalyticsInterface = mockk(relaxed = true)
    private val viewModel: AuthorizationViewModel = AuthorizationViewModel(analyticsUtils)

    @Test
    fun `When init is called, then should send qr_authorization_scrn__view analytics event`() {
        // When
        viewModel.init()

        // Then
        verify {
            analyticsUtils.sendEvent(AnalyticsEventScreen.QR_AUTHORIZATION_SCRN__VIEW.value)
        }
    }
}

