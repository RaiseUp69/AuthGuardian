package com.authguardian.mobileapp.fragment

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentNsdServerBinding
import com.authguardian.mobileapp.utils.UniversalUtils
import com.authguardian.mobileapp.viewmodel.NsdServerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NsdServerFragment : BaseFragment<FragmentNsdServerBinding>(FragmentNsdServerBinding::inflate), View.OnClickListener {

    private val viewModel: NsdServerViewModel by viewModels()

    // region data

    private var nsdManager: NsdManager? = null
    private var registrationListener: NsdManager.RegistrationListener? = null
    // endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.receivedMessage.collectLatest { message ->
                    binding.txtMessage.text = message
                }
                viewModel.isLoading.collectLatest { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                }

            }
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
            }

            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
                Log.d(TAG, "Service unregistered: ${serviceInfo.serviceName}")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "Unregistration failed with error code: $errorCode")
            }
        }

        nsdManager?.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    companion object {

        const val SERVICE_TYPE = "_http._tcp."
        private val TAG = NsdServerFragment::class.java.simpleName
    }
}