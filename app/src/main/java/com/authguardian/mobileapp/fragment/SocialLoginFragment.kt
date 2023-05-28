package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentSocialLoginBinding
import com.authguardian.mobileapp.viewmodel.SocialLoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton


class SocialLoginFragment : Fragment(), View.OnClickListener {

    private val viewModel: SocialLoginViewModel by viewModels()

    private var _binding: FragmentSocialLoginBinding? = null
    private val binding get() = _binding!!

    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val googleSignIn = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignIn)

        with(binding) {
            with(googleSignInButton) {
                setSize(SignInButton.SIZE_ICON_ONLY)
                setOnClickListener(this@SocialLoginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.googleSignInButton -> {
                viewModel.onGoogleSignInClicked()
            }
        }
    }
}