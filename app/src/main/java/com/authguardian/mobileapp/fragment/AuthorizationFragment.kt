package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.authguardian.mobileapp.databinding.FragmentAuthorizationBinding
import com.authguardian.mobileapp.viewmodel.AuthorizationViewModel

class AuthorizationFragment : BaseFragment<FragmentAuthorizationBinding>(FragmentAuthorizationBinding::inflate) {

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
    }
}