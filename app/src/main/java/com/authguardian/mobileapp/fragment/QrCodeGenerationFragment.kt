package com.authguardian.mobileapp.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.constant.Extra
import com.authguardian.mobileapp.constant.QrCode.USER_PASSWORD
import com.authguardian.mobileapp.constant.QrCode.USER_SSID
import com.authguardian.mobileapp.databinding.FragmentQrCodeGenerationBinding
import com.authguardian.mobileapp.provider.DataStoreRepositoryProvider
import com.authguardian.mobileapp.utils.AnalyticsUtils
import com.authguardian.mobileapp.utils.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.QrCodeGenerationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QrCodeGenerationFragment : BaseFragment<FragmentQrCodeGenerationBinding>(FragmentQrCodeGenerationBinding::inflate), View.OnClickListener {

    @Suppress("UNCHECKED_CAST")
    private val viewModel: QrCodeGenerationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = QrCodeGenerationViewModel(
                requireActivity().application,
                DataStoreRepositoryProvider.instance.getInstance(requireContext()),
                AnalyticsUtils,
            ) as T
        }
    }

    private var isCameraPermissionDeniedPermanently: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.init(ssid = USER_SSID, password = USER_PASSWORD)) {
            findNavController().popBackStack()
        }

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.qrCode.collectLatest { qrCode ->
                        imgQrCode.setImageBitmap(qrCode)
                        btnSaveQrCode.isVisible = true
                    }

                    viewModel.isLoading.collectLatest { isLoading ->
                        spinner.isVisible = isLoading
                    }

                    viewModel.savedMessage.collectLatest { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            btnScanQrCode.setOnClickListener(this@QrCodeGenerationFragment)
            btnSaveQrCode.setOnClickListener(this@QrCodeGenerationFragment)
        }
        checkCameraPermission()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            with(binding) {
                txtPermissionRejectedMessage.isVisible = false
                imgArrowGuide.isVisible = false
                qrCodeLayout.alpha = 1F
                btnScanQrCode.text = getString(R.string.i_want_scan)
            }
            isCameraPermissionDeniedPermanently = false
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnScanQrCode -> handleBtnScanQrCodeClick()
            R.id.btnSaveQrCode -> viewModel.saveUserData()
        }
    }

    private fun handleBtnScanQrCodeClick() {
        if (isCameraPermissionDeniedPermanently) {
            goToSettings()
        } else {
            navigateToRequestCameraPermission()
        }
    }

    private fun goToSettings() {
        requireActivity().let {
            it.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${it.packageName}")
                )
            )
        }
    }

    private fun navigateToRequestCameraPermission() {
        navigate(findNavController(), QrCodeGenerationFragmentDirections.actionQrCodeGenerationFragmentToRequestCameraPermissionFragment())
    }

    private fun checkCameraPermission() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>(Extra.PERMISSION_DENIED_PERMANENTLY)
            ?.observe(viewLifecycleOwner) { isPermissionDeniedPermanently ->
                handlePermissionDenied(isPermissionDeniedPermanently)
            }
    }

    private fun handlePermissionDenied(isPermissionDeniedPermanently: Boolean) {
        with(binding) {
            txtPermissionRejectedMessage.isVisible = true
            imgArrowGuide.isVisible = true
            txtPermissionRejectedMessage.text = getString(
                if (isPermissionDeniedPermanently)
                    R.string.camera_permission_rejected_second_time
                else
                    R.string.camera_permission_rejected_first_time
            )
            qrCodeLayout.alpha = 0.1F
            isCameraPermissionDeniedPermanently = isPermissionDeniedPermanently
            btnScanQrCode.text = getString(
                if (isPermissionDeniedPermanently)
                    R.string.go_to_settings
                else
                    R.string.i_want_scan
            )
        }
    }
}