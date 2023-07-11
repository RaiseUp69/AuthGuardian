package com.authguardian.mobileapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentSocialLoginBinding
import com.authguardian.mobileapp.utils.NavigationUtils
import com.authguardian.mobileapp.viewmodel.SocialLoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar

class SocialLoginFragment : BaseFragment<FragmentSocialLoginBinding>(FragmentSocialLoginBinding::inflate), View.OnClickListener {

    private val viewModel: SocialLoginViewModel by viewModels()

    private var googleSignInClient: GoogleSignInClient? = null

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
            viewModel.isSignInAvailable(requireContext())
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isSignInAvailable(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()

        val googleSignIn = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignIn)

        with(binding) {
            viewModel.isGoogleSignInAvailable.observe(viewLifecycleOwner) { isGoogleSignInAvailable ->
                if (isGoogleSignInAvailable) {
                    with(googleSignInButton) {
                        visibility = View.VISIBLE
                        setSize(SignInButton.SIZE_ICON_ONLY)
                    }
                    googleSignOutButton.visibility = View.GONE
                } else {
                    googleSignInButton.visibility = View.GONE
                    googleSignOutButton.visibility = View.VISIBLE
                }
            }
            googleSignInButton.setOnClickListener(this@SocialLoginFragment)
            googleSignOutButton.setOnClickListener(this@SocialLoginFragment)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.googleSignInButton -> onGoogleSignInClicked()
            R.id.googleSignOutButton -> onGoogleSignOutClicked()
        }
    }

    private fun onGoogleSignInClicked() {
        viewModel.onGoogleSignInClicked()
        val signInIntent: Intent? = googleSignInClient?.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun onGoogleSignOutClicked() {
        viewModel.onGoogleSignOutClicked()
        googleSignInClient?.signOut()?.addOnCompleteListener(requireActivity()) {
            viewModel.isSignInAvailable(requireContext())
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            NavigationUtils.navigate(findNavController(), SocialLoginFragmentDirections.actionSocialLoginFragmentToGoogleSignSuccessfulFragment())
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the [GoogleSignInStatusCodes] class reference for more information.
            Snackbar.make(
                binding.root,
                "signInResult:failed code=" + e.statusCode,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}