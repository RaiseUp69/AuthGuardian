package com.authguardian.mobileapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.consts.QrCode.USER_PASSWORD
import com.authguardian.mobileapp.consts.QrCode.USER_SSID
import com.authguardian.mobileapp.databinding.FragmentQrCodeGenerationFragmentBinding
import com.authguardian.mobileapp.viewmodels.QrCodeGenerationViewModel

class QrCodeGenerationFragment : Fragment(), View.OnClickListener {

    private val viewModel: QrCodeGenerationViewModel by viewModels()

    private var _binding: FragmentQrCodeGenerationFragmentBinding? = null
    private val binding get() = _binding!!

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

        viewModel.qrCode.observe(viewLifecycleOwner) { qrCode ->
            binding.imgBarcode.setImageBitmap(qrCode)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbLoading.isVisible = isLoading
        }

        binding.btnScanQrCode?.setOnClickListener(this@QrCodeGenerationFragment)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnScanQrCode -> {
                findNavController().navigate(QrCodeGenerationFragmentDirections.actionQrCodeGenerationFragmentToQrCodeScanner())
            }
        }
    }
}