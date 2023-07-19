package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.adapter.DatabaseAdapter
import com.authguardian.mobileapp.databinding.FragmentDatabaseBinding
import com.authguardian.mobileapp.module.NetworkModule
import com.authguardian.mobileapp.utils.CoroutineDispatchersProvider
import com.authguardian.mobileapp.utils.Network
import com.authguardian.mobileapp.viewmodel.DatabaseViewModel

class DatabaseFragment : BaseFragment<FragmentDatabaseBinding>(FragmentDatabaseBinding::inflate), View.OnClickListener {

    private val viewModel: DatabaseViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val okHttpInstance = NetworkModule().getOkHttpClientInstance(requireActivity().application)
                val messageService = Network.getMessageServiceInstance(Network.getRetrofitInstance(okHttpInstance))
                return DatabaseViewModel(requireActivity().application, DatabaseAdapter(), messageService, dispatcher = CoroutineDispatchersProvider()) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        viewModel.init()

        with(binding) {
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                pbLoading.isVisible = isLoading
            }

            with(rvMessages) {
                adapter = viewModel.adapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_refresh, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                R.id.action_refresh -> {
                    viewModel.onRefreshClicked()
                    true
                }

                else -> true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onClick(v: View?) {}
}