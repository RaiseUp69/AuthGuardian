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
import com.authguardian.mobileapp.databinding.FragmentNetworkServiceDiscoverBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class NetworkServiceDiscoverFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentNetworkServiceDiscoverBinding? = null
    private val binding get() = _binding!!

    private lateinit var nsdManager: NsdManager
    private lateinit var registrationListener: NsdManager.RegistrationListener
    private lateinit var discoveryListener: NsdManager.DiscoveryListener
    private lateinit var resolveListener: NsdManager.ResolveListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkServiceDiscoverBinding.inflate(inflater, container, false)

        nsdManager = requireContext().getSystemService(Context.NSD_SERVICE) as NsdManager
        setupListeners()
        binding.btnClient.setOnClickListener(this@NetworkServiceDiscoverFragment)
        binding.btnServer.setOnClickListener(this@NetworkServiceDiscoverFragment)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nsdManager.stopServiceDiscovery(discoveryListener)
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnClient -> {
                nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
            }

            binding.btnServer -> {
                // Server code
                val uniqueServiceName = "${Build.BRAND} ${Build.MODEL}"

                val serviceInfo = NsdServiceInfo().apply {
                    serviceName = uniqueServiceName
                    serviceType = SERVICE_TYPE
                    port = 12345
                }

                nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
                startServerSocket(serviceInfo.port)
            }
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

        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                Log.d("NSD", "Service discovery started")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                Log.d("NSD", "Service found: ${serviceInfo.serviceName}")
                nsdManager.resolveService(serviceInfo, resolveListener)
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

                if (serviceInfo.serviceName == SERVICE_NAME) {
                    Log.d("NSD", "Same machine: $SERVICE_NAME; host ${serviceInfo.host}")
                    return
                }

                startClientSocket(serviceInfo.host, serviceInfo.port)
            }
        }
    }

    private fun startServerSocket(port: Int) {
        Thread {
            try {
                val serverSocket = ServerSocket(port)
                val socket = serverSocket.accept()

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = PrintWriter(socket.getOutputStream(), true)

                output.println("Hello from server!")
                val receivedData = input.readLine()
                binding.txtReceiverMessage.text = receivedData
                Log.d("NSD", "Received data from client: $receivedData")

                socket.close()
                serverSocket.close()
            } catch (e: IOException) {
                Log.e("NSD", "Server socket error: ", e)
            }
        }.start()
    }

    private fun startClientSocket(address: InetAddress, port: Int) {
        Thread {
            try {
                val socket = Socket(address, port)

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = PrintWriter(socket.getOutputStream(), true)

                output.println("Hello from client!")

                val receivedData = input.readLine()
                binding.txtReceiverMessage.text = receivedData
                Log.d("NSD", "Received data from server: $receivedData")

                socket.close()
            } catch (e: IOException) {
                Log.e("NSD", "Client socket error: ", e)
            }
        }.start()
    }

    companion object {

        private const val SERVICE_NAME = "MyApp"
        private const val SERVICE_TYPE = "_http._tcp."
    }
}
