package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentNsdChooseRoleBinding
import com.authguardian.mobileapp.utils.NavigationUtils.navigate
import com.authguardian.mobileapp.viewmodel.NsdChooseRoleViewModel

class NsdChooseRoleFragment : BaseFragment<FragmentNsdChooseRoleBinding>(FragmentNsdChooseRoleBinding::inflate), View.OnClickListener {

    private val viewModel: NsdChooseRoleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()
        binding.btnClient.setOnClickListener(this@NsdChooseRoleFragment)
        binding.btnServer.setOnClickListener(this@NsdChooseRoleFragment)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnClient -> {
                navigate(findNavController(), NsdChooseRoleFragmentDirections.actionNsdChooseRoleFragmentToNsdClientFragment())
            }

            R.id.btnServer -> {
                navigate(findNavController(), NsdChooseRoleFragmentDirections.actionNsdChooseRoleFragmentToNsdServerFragment())
            }
        }
    }
}
