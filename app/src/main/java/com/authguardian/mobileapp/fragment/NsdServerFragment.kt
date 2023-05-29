package com.authguardian.mobileapp.fragment

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentNsdServerBinding
import com.authguardian.mobileapp.utils.UniversalUtils
import com.authguardian.mobileapp.viewmodel.NsdServerViewModel

class NsdServerFragment : Fragment(), View.OnClickListener {

    private val viewModel: NsdServerViewModel by viewModels()
    private var _binding: FragmentNsdServerBinding? = null
    private val binding get() = _binding!!

    // region data

    private var nsdManager: NsdManager? = null
    private var registrationListener: NsdManager.RegistrationListener? = null
    // endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNsdServerBinding.inflate(inflater, container, false)

        nsdManager = requireContext().getSystemService(Context.NSD_SERVICE) as NsdManager

        val serviceInfo = NsdServiceInfo().apply {
            serviceName = UniversalUtils.getDeviceBrandAndModel()
            serviceType = SERVICE_TYPE
            port = 12345
        }

        setupRegistrationListener(serviceInfo)

        if (!viewModel.init(serviceInfo.port)) {
            findNavController().popBackStack()
        }

        viewModel.startServerSocket()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.receivedMessage.observe(viewLifecycleOwner) { message ->
            binding.txtMessage.text = message
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        binding.btnBack.setOnClickListener(this@NsdServerFragment)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnBack -> {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nsdManager?.unregisterService(registrationListener)
        registrationListener = null
        nsdManager = null
    }

    private fun setupRegistrationListener(serviceInfo: NsdServiceInfo) {
        registrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
                Log.d(TAG, "Service registered: ${serviceInfo.serviceName}")
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "Registration failed with error code: $errorCode")
                // Handle registration failure
            }

            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
                Log.d(TAG, "Service unregistered: ${serviceInfo.serviceName}")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "Unregistration failed with error code: $errorCode")
                // Handle unregistration failure
            }
        }

        nsdManager?.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    companion object {

        const val SERVICE_TYPE = "_http._tcp."
        private val TAG = NsdServerFragment::class.java.simpleName
    }
}