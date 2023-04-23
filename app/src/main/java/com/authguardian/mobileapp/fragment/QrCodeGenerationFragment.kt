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
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.const.Extra
import com.authguardian.mobileapp.const.QrCode.USER_PASSWORD
import com.authguardian.mobileapp.const.QrCode.USER_SSID
import com.authguardian.mobileapp.databinding.FragmentQrCodeGenerationFragmentBinding
import com.authguardian.mobileapp.extension.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.QrCodeGenerationViewModel

class QrCodeGenerationFragment : Fragment(), View.OnClickListener {

    private val viewModel: QrCodeGenerationViewModel by viewModels()

    private var _binding: FragmentQrCodeGenerationFragmentBinding? = null
    private val binding get() = _binding!!

    private var needToGoSettings: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeGenerationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
        checkCameraPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        handleResume()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnScanQrCode -> handleBtnScanQrCodeClick()
        }
    }

    private fun initViewModel() {
        if (!viewModel.init(ssid = USER_SSID, password = USER_PASSWORD)) {
            findNavController().popBackStack()
        }

        viewModel.qrCode.observe(viewLifecycleOwner) { qrCode ->
            binding.imgQrCode.setImageBitmap(qrCode)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.spinner.isVisible = isLoading
        }
    }

    private fun initViews() {
        binding.btnScanQrCode.setOnClickListener(this)
    }

    private fun handleResume() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            updateUiForPermissionGranted()
        }
    }

    private fun updateUiForPermissionGranted() {
        with(binding) {
            txtPermissionRejectedMessage.isVisible = false
            imgArrowGuide.isVisible = false
            qrCodeLayout.alpha = 1F
            btnScanQrCode.text = getString(R.string.i_want_scan)
        }
        needToGoSettings = false
    }

    private fun handleBtnScanQrCodeClick() {
        if (needToGoSettings) {
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
            needToGoSettings = isPermissionDeniedPermanently
            btnScanQrCode.text = getString(
                if (isPermissionDeniedPermanently)
                    R.string.go_to_settings
                else
                    R.string.i_want_scan
            )
        }
    }
}