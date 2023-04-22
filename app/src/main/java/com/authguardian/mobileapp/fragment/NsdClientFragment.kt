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
import com.authguardian.mobileapp.databinding.FragmentNsdClientBinding
import com.authguardian.mobileapp.utils.UniversalUtils.getDeviceBrandAndModel
import com.authguardian.mobileapp.viewmodel.NsdClientViewModel
import com.google.android.material.snackbar.Snackbar

class NsdClientFragment : Fragment(), View.OnClickListener {

    private val viewModel: NsdClientViewModel by viewModels()

    private var _binding: FragmentNsdClientBinding? = null
    private val binding get() = _binding!!

    // region nsd

    private lateinit var nsdManager: NsdManager
    private var discoveryListener: NsdManager.DiscoveryListener? = null
    private var resolveListener: NsdManager.ResolveListener? = null
    // endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNsdClientBinding.inflate(inflater, container, false)

        nsdManager = requireContext().getSystemService(Context.NSD_SERVICE) as NsdManager
        setupListeners()
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nsdManager.stopServiceDiscovery(discoveryListener)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()

        viewModel.receivedMessage.observe(viewLifecycleOwner) { message ->
            binding.txtMessage.text = message
        }

        binding.btnBack.setOnClickListener(this@NsdClientFragment)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupListeners() {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                Log.d("NSD", "Service discovery started")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                Log.d("NSD", "Service found: ${serviceInfo.serviceName}")
                try {
                    nsdManager.resolveService(serviceInfo, resolveListener)
                } catch (e: Exception) {
                    Snackbar.make(requireView(), e.localizedMessage ?: getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                Log.e("NSD", "Service lost: ${serviceInfo.serviceName}")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i("NSD", "Discovery stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NSD", "Discovery failed: Error code: $errorCode")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NSD", "Discovery failed: Error code: $errorCode")
                nsdManager.stopServiceDiscovery(this)
            }
        }

        resolveListener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NSD", "Resolve failed with error code: $errorCode")
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                Log.d("NSD", "Service resolved: ${serviceInfo.serviceName}")

                if (serviceInfo.serviceName == getDeviceBrandAndModel()) {
                    Log.d("NSD", "Same machine: ${getDeviceBrandAndModel()}")
                    return
                }

                viewModel.startClientSocket(serviceInfo.host, serviceInfo.port)
            }
        }
    }

    companion object {

        private const val SERVICE_TYPE = "_http._tcp."
    }
}