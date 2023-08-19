package com.authguardian.mobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.adapter.DatabaseAdapter
import com.authguardian.mobileapp.service.MessageService
import com.authguardian.mobileapp.utils.CoroutineDispatchersProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseViewModel(
    application: Application,
    val adapter: DatabaseAdapter,
    private val messageService: MessageService,
    private val dispatcher: CoroutineDispatchersProvider
) : AndroidViewModel(application) {

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun init() {
        updateData()
    }

    fun onRefreshClicked() {
        updateData()
    }

    fun updateData() = viewModelScope.launch(dispatcher.IO) {
        try {
            _isLoading.value = true
            val messages = messageService.getMessages()

            val items = messages.map {
                DatabaseAdapter.Item.DatabaseMessageItem(it.text)
            }

            withContext(dispatcher.Main) {
                adapter.submitList(items)
                _isLoading.value = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
