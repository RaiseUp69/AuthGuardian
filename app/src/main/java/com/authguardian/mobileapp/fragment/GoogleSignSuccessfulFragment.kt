package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.authguardian.mobileapp.databinding.FragmentGoogleSignSuccessfulBinding
import com.authguardian.mobileapp.viewmodel.GoogleSignInSuccessfulViewModel

class GoogleSignSuccessfulFragment : BaseFragment<FragmentGoogleSignSuccessfulBinding>(FragmentGoogleSignSuccessfulBinding::inflate) {

    private val viewModel: GoogleSignInSuccessfulViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()
    }
}