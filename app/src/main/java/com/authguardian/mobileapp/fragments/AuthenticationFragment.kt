package com.authguardian.mobileapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.databinding.FragmentAuthenticationBinding
import com.authguardian.mobileapp.viewmodels.AuthenticationViewModel

class AuthenticationFragment : Fragment() {

    companion object {

        /*        @JvmStatic
                fun newInstance() = AuthenticationFragment()*/
    }

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
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
            val action = AuthenticationFragmentDirections.actionAuthenticationFragmentToAuthorizationFragment()
            findNavController().navigate(action)
        }
    }
}