package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentAuthMethodBinding
import com.authguardian.mobileapp.utils.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.AuthMethodViewModel

class AuthMethodFragment : BaseFragment<FragmentAuthMethodBinding>(FragmentAuthMethodBinding::inflate), View.OnClickListener {

    private val viewModel: AuthMethodViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()

        with(binding) {
            btnQrCode.setOnClickListener(this@AuthMethodFragment)
            btnNsd.setOnClickListener(this@AuthMethodFragment)
            btnSocialLogin.setOnClickListener(this@AuthMethodFragment)
            btnNavigateToDatabase.setOnClickListener(this@AuthMethodFragment)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnQrCode -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToQrCodeAuthenticationGraph()
                )
            }

            R.id.btnNsd -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToNsdAuthenticationGraph()
                )
            }

            R.id.btnSocialLogin -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToSocialLoginGraph()
                )
            }

            R.id.btnNavigateToDatabase -> {
                navigate(
                    findNavController(),
                    AuthMethodFragmentDirections.actionAuthMethodFragmentToServerDataGraph()
                )
            }
        }
    }
}