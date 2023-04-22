package com.authguardian.mobileapp.fragment

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.databinding.FragmentNsdServerBinding
import com.authguardian.mobileapp.utils.NsdServiceUtils.Companion.SERVICE_TYPE
import com.authguardian.mobileapp.viewmodel.NsdServerViewModel

class NsdServerFragment : Fragment() {

    private val viewModel: NsdServerViewModel by viewModels()

    private var _binding: FragmentNsdServerBinding? = null
    private val binding get() = _binding!!

    // region nsd

    private lateinit var nsdManager: NsdManager
    private lateinit var registrationListener: NsdManager.RegistrationListener
    // endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNsdServerBinding.inflate(inflater, container, false)

        nsdManager = requireContext().getSystemService(Context.NSD_SERVICE) as NsdManager
        val uniqueServiceName = "${Build.BRAND} ${Build.MODEL}"
        val serviceInfo = NsdServiceInfo().apply {
            serviceName = uniqueServiceName
            serviceType = SERVICE_TYPE
            port = 12345
        }
        setupListeners()
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)

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
    }

    private fun setupListeners() {
        registrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
                Log.d("NSD", "Service registered: ${serviceInfo.serviceName}")
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NSD", "Registration failed with error code: $errorCode")
            }

            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
                Log.d("NSD", "Service unregistered: ${serviceInfo.serviceName}")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NSD", "Unregistration failed with error code: $errorCode")
            }
        }
    }
}