package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.authguardian.mobileapp.databinding.FragmentSocialLoginBinding
import com.authguardian.mobileapp.viewmodel.SocialLoginViewModel

class SocialLoginFragment : Fragment() {

    private val viewModel: SocialLoginViewModel by viewModels()

    private var _binding: FragmentSocialLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}