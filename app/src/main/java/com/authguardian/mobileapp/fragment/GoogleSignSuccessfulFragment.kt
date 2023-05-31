package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.authguardian.mobileapp.databinding.FragmentGoogleSignSuccessfulBinding
import com.authguardian.mobileapp.viewmodel.GoogleSignInSuccessfulViewModel

class GoogleSignSuccessfulFragment : Fragment() {

    private val viewModel: GoogleSignInSuccessfulViewModel by viewModels()

    private var _binding: FragmentGoogleSignSuccessfulBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleSignSuccessfulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}