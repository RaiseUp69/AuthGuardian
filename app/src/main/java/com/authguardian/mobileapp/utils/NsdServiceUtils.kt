package com.authguardian.mobileapp.utils

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.DiscoveryListener
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import android.util.Log

class NsdServiceUtils(private val context: Context) {

    private val nsdManager: NsdManager
        get() = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private var resolveListener: NsdManager.ResolveListener? = null
    private var discoveryListener: DiscoveryListener? = null
    private var registrationListener: RegistrationListener? = null
    private var serviceName = "NsdChat"
    private var chosenServiceInfo: NsdServiceInfo? = null

    fun initNsd() {
        initResolveListener()
    }

    private fun initDiscoveryListener() {
        discoveryListener = object : DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                Log.d(TAG, "Service discovery started")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                Log.d(TAG, "Service discovery success$service")
                if (service.serviceType != SERVICE_TYPE) {
                    Log.d(TAG, "Unknown Service Type: " + service.serviceType)
                } else if (service.serviceName == serviceName) {
                    Log.d(TAG, "Same machine: $serviceName")
                } else if (service.serviceName.contains(serviceName)) {
                    nsdManager.resolveService(service, resolveListener)
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                Log.e(TAG, "service lost$service")
                if (chosenServiceInfo == service) {
                    chosenServiceInfo = null
                }
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i(TAG, "Discovery stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
            }
        }
    }

    private fun initResolveListener() {
        resolveListener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "Resolve failed$errorCode")
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                Log.e(TAG, "Resolve Succeeded. $serviceInfo")
                if (serviceInfo.serviceName == serviceName) {
                    Log.d(TAG, "Same IP.")
                    return
                }
                chosenServiceInfo = serviceInfo
            }
        }
    }

    private fun initRegistrationListener() {
        registrationListener = object : RegistrationListener {
            override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
                serviceName = NsdServiceInfo.serviceName
                Log.d(TAG, "Service registered: $serviceName")
            }

            override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
                Log.d(TAG, "Service registration failed: $arg1")
            }

            override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                Log.d(TAG, "Service unregistered: " + arg0.serviceName)
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.d(TAG, "Service unregistration failed: $errorCode")
            }
        }
    }

    fun registerService(port: Int) {
        tearDown() // Cancel any previous registration request
        initRegistrationListener()
        val serviceInfo = NsdServiceInfo()
        serviceInfo.port = port
        serviceInfo.serviceName = serviceName
        serviceInfo.serviceType = SERVICE_TYPE
        nsdManager.registerService(
            serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener
        )
    }

    fun discoverServices() {
        stopDiscovery() // Cancel any existing discovery request
        initDiscoveryListener()
        nsdManager.discoverServices(
            SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener
        )
    }

    fun stopDiscovery() {
        if (discoveryListener != null) {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener)
            } finally {
            }
            discoveryListener = null
        }
    }

    private fun tearDown() {
        if (registrationListener != null) {
            try {
                nsdManager.unregisterService(registrationListener)
            } finally {
            }
            registrationListener = null
        }
    }

    companion object {
        // From work notebook
        const val SERVICE_TYPE = "_http._tcp."
        private val TAG = NsdServiceUtils::class.java.simpleName
    }
}