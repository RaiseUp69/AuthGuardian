package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.authguardian.mobileapp.databinding.FragmentGoogleSignSuccessfulBinding

class GoogleSignSuccessfulFragment : Fragment() {

    private var _binding: FragmentGoogleSignSuccessfulBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleSignSuccessfulBinding.inflate(inflater, container, false)
        return binding.root
    }
}