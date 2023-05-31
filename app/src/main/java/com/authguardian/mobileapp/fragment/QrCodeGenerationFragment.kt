package com.authguardian.mobileapp.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.const.Extra
import com.authguardian.mobileapp.const.QrCode.USER_PASSWORD
import com.authguardian.mobileapp.const.QrCode.USER_SSID
import com.authguardian.mobileapp.databinding.FragmentQrCodeGenerationBinding
import com.authguardian.mobileapp.provider.DataStoreRepositoryProvider
import com.authguardian.mobileapp.utils.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.QrCodeGenerationViewModel
import com.google.android.material.snackbar.Snackbar

class QrCodeGenerationFragment : Fragment(), View.OnClickListener {

    @Suppress("UNCHECKED_CAST")
    private val viewModel: QrCodeGenerationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = QrCodeGenerationViewModel(
                requireActivity().application,
                DataStoreRepositoryProvider.instance.getInstance(requireContext())
            ) as T
        }
    }

    private var _binding: FragmentQrCodeGenerationBinding? = null
    private val binding get() = _binding!!

    private var isCameraPermissionDeniedPermanently: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeGenerationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.init(ssid = USER_SSID, password = USER_PASSWORD)) {
            findNavController().popBackStack()
        }

        viewModel.qrCode.observe(viewLifecycleOwner) { qrCode ->
            binding.imgQrCode.setImageBitmap(qrCode)
            binding.btnSaveQrCode.isVisible = true
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.spinner.isVisible = isLoading
        }

        viewModel.savedMessage.observe(viewLifecycleOwner) { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }

        binding.btnScanQrCode.setOnClickListener(this)
        binding.btnSaveQrCode.setOnClickListener(this)
        checkCameraPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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