package com.authguardian.mobileapp.fragment

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.const.QrCode
import com.authguardian.mobileapp.databinding.FragmentQrCodeScannerBinding
import com.authguardian.mobileapp.utils.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.QrCodeScannerViewModel
import com.authguardian.mobileapp.viewmodel.QrCodeScannerViewModel.Companion.TAG
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.Barcode.QR_CODE
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.snackbar.Snackbar

class QrCodeScannerFragment : Fragment() {

    private val viewModel: QrCodeScannerViewModel by viewModels()
    private var cameraSource: CameraSource? = null

    private var _binding: FragmentQrCodeScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        * For the case when user got this screen,
        * minimized the app,
        * deny camera permission,
        * and back into app
        * */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeScannerBinding.inflate(inflater, container, false)
        setupCamera()
        binding.surfaceView.holder.addCallback(callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }

    private val processor = object : Detector.Processor<Barcode> {

        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            detections.apply {
                if (detectedItems.isNotEmpty()) {
                    val qr = detectedItems.valueAt(0)
                    qr.wifi.let {
                        Log.d(TAG, "SSID: ${it.ssid}, Password: ${it.password}")
                        if (it.ssid == QrCode.USER_SSID && it.password == QrCode.USER_PASSWORD) {
                            navigate(findNavController(), QrCodeScannerFragmentDirections.actionQrCodeScannerFragmentToAuthorizationFragment())
                        }
                    }
                }
            }
        }

        override fun release() {}
    }

    private fun setupCamera() {
        BarcodeDetector.Builder(requireContext()).setBarcodeFormats(QR_CODE).build().apply {
            setProcessor(processor)
            if (!isOperational) {
                Log.d(TAG, "Native QR detector dependencies not available!")
                return
            }
            cameraSource = CameraSource.Builder(requireContext(), this)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK).build()
        }
    }

    private val callback = object : SurfaceHolder.Callback {
        @SuppressLint("MissingPermission")
        override fun surfaceCreated(holder: SurfaceHolder) {
            if (isPlayServicesAvailable(requireActivity())) cameraSource?.start(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource?.stop()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    }

    private fun isPlayServicesAvailable(activity: Activity): Boolean {
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext())
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(activity, code, code)?.show()
            Snackbar.make(binding.root, "PlayServices unavailable", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}