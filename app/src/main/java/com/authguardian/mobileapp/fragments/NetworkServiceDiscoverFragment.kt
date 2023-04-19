package com.authguardian.mobileapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.databinding.FragmentNetworkServiceDiscoverFragmentBinding
import com.authguardian.mobileapp.viewmodels.NetworkServiceDiscoverViewModel

class NetworkServiceDiscoverFragment : Fragment() {

    private val viewModel: NetworkServiceDiscoverViewModel by viewModels()

    private var _binding: FragmentNetworkServiceDiscoverFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkServiceDiscoverFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        binding.btnPushMe.setOnClickListener {
            val action = NetworkServiceDiscoverFragmentDirections.actionAuthenticationFragmentToAuthorizationFragment()
            findNavController().navigate(action)
        }
    }
}