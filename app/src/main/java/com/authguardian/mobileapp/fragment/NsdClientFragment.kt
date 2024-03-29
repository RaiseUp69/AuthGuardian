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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentNsdClientBinding
import com.authguardian.mobileapp.utils.UniversalUtils.getDeviceBrandAndModel
import com.authguardian.mobileapp.viewmodel.NsdClientViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NsdClientFragment : Fragment(), View.OnClickListener {

    private val viewModel: NsdClientViewModel by viewModels()

    private var _binding: FragmentNsdClientBinding? = null
    private val binding get() = _binding!!

    // region nsd
    private var nsdManager: NsdManager? = null
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
        nsdManager?.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nsdManager?.let { nsdManager ->
            nsdManager.stopServiceDiscovery(discoveryListener)
            discoveryListener = null
            resolveListener = null
        }
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
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
        binding.btnBack.setOnClickListener(this@NsdClientFragment)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnBack -> {
                findNavController().popBackStack()
                viewModel.onBackPressed()
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
                    nsdManager?.resolveService(serviceInfo, resolveListener)
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
                nsdManager?.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NSD", "Discovery failed: Error code: $errorCode")
                nsdManager?.stopServiceDiscovery(this)
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