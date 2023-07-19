package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentAuthMethodBinding
import com.authguardian.mobileapp.utils.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.AuthMethodViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AuthMethodFragment : BaseFragment<FragmentAuthMethodBinding>(FragmentAuthMethodBinding::inflate), OnClickListener {

    private val viewModel: AuthMethodViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()

        with(binding) {
            btnQrCode.setOnClickListener(this@AuthMethodFragment)
            btnNsd.setOnClickListener(this@AuthMethodFragment)
            btnSocialLogin.setOnClickListener(this@AuthMethodFragment)
            btnTestMessage.setOnClickListener(this@AuthMethodFragment)
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

            R.id.btnTestMessage -> {
                val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_text, null)
                val editText = dialogView.findViewById<EditText>(R.id.dialogEditText)

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(requireContext().getString(R.string.message_to_server))
                    .setMessage(requireContext().getString(R.string.please_enter_message))
                    .setView(dialogView)
                    .setPositiveButton(R.string.send) { dialog, _ ->
                        viewModel.postMessage(requireContext(), editText.text.toString())
                        dialog.dismiss()
                    }
                    .create()
                    .show()

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