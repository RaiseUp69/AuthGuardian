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
import com.authguardian.mobileapp.viewmodel.QrCodeGenerationViewModel

class QrCodeGenerationFragment : Fragment(), View.OnClickListener {

    private var needToGoSettings: Boolean = false
    private val viewModel: QrCodeGenerationViewModel by viewModels()

    private var _binding: FragmentQrCodeGenerationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        // When user back to app from settings
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            with(binding) {
                txtPermissionRejectedMessage.isVisible = false
                qrCodeLayout.alpha = 1F
                btnScanQrCode.text = getString(R.string.i_want_scan)
            }
            needToGoSettings = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeGenerationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(ssid = USER_SSID, password = USER_PASSWORD)

        with(binding) {
            viewModel.qrCode.observe(viewLifecycleOwner) { qrCode ->
                imgQrCode.setImageBitmap(qrCode)
            }
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                spinner.isVisible = isLoading
            }

            btnScanQrCode.setOnClickListener(this@QrCodeGenerationFragment)
        }

        checkCameraPermission()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnScanQrCode -> {
                if (needToGoSettings) {
                    requireActivity().let {
                        it.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${it.packageName}")
                            )
                        )
                    }
                } else {
                    findNavController().navigate(QrCodeGenerationFragmentDirections.actionQrCodeGenerationFragmentToRequestCameraPermissionFragment())
                }
            }
        }
    }

    private fun checkCameraPermission() = with(binding) {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>(Extra.PERMISSION_DENIED_PERMANENTLY)
            ?.observe(viewLifecycleOwner) { isPermissionDeniedPermanently ->
                if (isPermissionDeniedPermanently) {
                    txtPermissionRejectedMessage.isVisible = true
                    txtPermissionRejectedMessage.text = getString(R.string.camera_permission_rejected_second_time)
                    qrCodeLayout.alpha = 0.1F
                    needToGoSettings = true
                    btnScanQrCode.text = getString(R.string.go_to_settings)
                } else {
                    txtPermissionRejectedMessage.isVisible = true
                    txtPermissionRejectedMessage.text = getString(R.string.camera_permission_rejected_first_time)
                    qrCodeLayout.alpha = 0.1F
                    needToGoSettings = false
                }
            }
    }
}