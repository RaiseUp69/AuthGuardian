package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.authguardian.mobileapp.databinding.FragmentAuthorizationBinding
import com.authguardian.mobileapp.utils.AnalyticsUtils
import com.authguardian.mobileapp.viewmodel.AuthorizationViewModel

class AuthorizationFragment : BaseFragment<FragmentAuthorizationBinding>(FragmentAuthorizationBinding::inflate) {

    @Suppress("UNCHECKED_CAST")
    private val viewModel: AuthorizationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthorizationViewModel(
                AnalyticsUtils
            ) as T
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
    }
}