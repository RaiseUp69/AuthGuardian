package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.View
import com.authguardian.mobileapp.databinding.FragmentGoogleSignSuccessfulBinding
import com.authguardian.mobileapp.viewmodel.GoogleSignInSuccessfulViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.android.x.viewmodel.viewModel

class GoogleSignSuccessfulFragment : BaseFragment<FragmentGoogleSignSuccessfulBinding>(FragmentGoogleSignSuccessfulBinding::inflate), DIAware {

    override val di: DI by closestDI()
    private val viewModel: GoogleSignInSuccessfulViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()
    }
}