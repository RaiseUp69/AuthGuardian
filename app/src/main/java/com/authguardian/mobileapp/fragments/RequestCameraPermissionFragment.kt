package com.authguardian.mobileapp.fragments

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.consts.Extra.PERMISSION_DENIED_PERMANENTLY

class RequestCameraPermissionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
            findNavController().navigate(RequestCameraPermissionFragmentDirections.actionRequestCameraPermissionFragmentToQrCodeScannerFragment())
        } else {
            requestPermission.launch(CAMERA)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_request_camera_permission, container, false)

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            findNavController().navigate(RequestCameraPermissionFragmentDirections.actionRequestCameraPermissionFragmentToQrCodeScannerFragment())
        } else {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                PERMISSION_DENIED_PERMANENTLY, !shouldShowRequestPermissionRationale(CAMERA)
            )
            findNavController().popBackStack()
        }
    }
}