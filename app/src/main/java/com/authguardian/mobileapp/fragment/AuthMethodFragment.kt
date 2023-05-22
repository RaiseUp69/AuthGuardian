package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.databinding.FragmentAuthMethodBinding
import com.authguardian.mobileapp.extension.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.AuthMethodViewModel

class AuthMethodFragment : Fragment(), OnClickListener {

    private val viewModel: AuthMethodViewModel by viewModels()

    private var _binding: FragmentAuthMethodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        binding.btnQrCode.setOnClickListener(this@AuthMethodFragment)
        binding.btnNsd.setOnClickListener(this@AuthMethodFragment)
        binding.btnSocialLogin.setOnClickListener(this@AuthMethodFragment)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnQrCode -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToQrCodeAuthenticationGraph()
                )
            }

            binding.btnNsd -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToNsdAuthenticationGraph()
                )
            }

            binding.btnSocialLogin -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToSocialLoginGraph()
                )
            }
        }
    }
}