package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.authguardian.mobileapp.databinding.FragmentNsdChooseRoleBinding
import com.authguardian.mobileapp.extension.NavigationUtils.navigate

class NsdChooseRoleFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentNsdChooseRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNsdChooseRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClient.setOnClickListener(this@NsdChooseRoleFragment)
        binding.btnServer.setOnClickListener(this@NsdChooseRoleFragment)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnClient -> {
                navigate(findNavController(), NsdChooseRoleFragmentDirections.actionNsdChooseRoleFragmentToNsdClientFragment())
            }

            binding.btnServer -> {
                navigate(findNavController(), NsdChooseRoleFragmentDirections.actionNsdChooseRoleFragmentToNsdServerFragment())
            }
        }
    }
}
