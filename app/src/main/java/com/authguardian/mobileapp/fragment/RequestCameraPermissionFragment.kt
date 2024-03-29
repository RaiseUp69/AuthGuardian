package com.authguardian.mobileapp.fragment

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
import com.authguardian.mobileapp.constant.Extra.PERMISSION_DENIED_PERMANENTLY
import com.authguardian.mobileapp.utils.NavigationUtils.navigate

class RequestCameraPermissionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
            navigate(findNavController(), RequestCameraPermissionFragmentDirections.actionRequestCameraPermissionFragmentToQrCodeScannerFragment())
        } else {
            requestPermission.launch(CAMERA)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_request_camera_permission, container, false)

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            navigate(findNavController(), RequestCameraPermissionFragmentDirections.actionRequestCameraPermissionFragmentToQrCodeScannerFragment())
        } else {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                PERMISSION_DENIED_PERMANENTLY, !shouldShowRequestPermissionRationale(CAMERA)
            )
            findNavController().popBackStack()
        }
    }
}