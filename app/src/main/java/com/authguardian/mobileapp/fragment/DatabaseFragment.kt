package com.authguardian.mobileapp.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.FragmentDatabaseBinding
import com.authguardian.mobileapp.viewmodel.DatabaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.android.x.viewmodel.viewModel

class DatabaseFragment : BaseFragment<FragmentDatabaseBinding>(FragmentDatabaseBinding::inflate),
    View.OnClickListener, DIAware {

    override val di: DI by closestDI()
    private val viewModel: DatabaseViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        viewModel.init()

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isLoading.collectLatest {
                        pbLoading.isVisible = it
                    }
                }
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