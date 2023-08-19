package com.authguardian.mobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.authguardian.mobileapp.adapter.DatabaseAdapter
import com.authguardian.mobileapp.repository.MessageRepository
import com.authguardian.mobileapp.utils.CoroutineDispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseViewModel(
    application: Application,
    val adapter: DatabaseAdapter,
    private val dispatcher: CoroutineDispatchersProvider,
    private val messageRepository: MessageRepository
) : AndroidViewModel(application) {

    private var _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()

    fun init() {
        updateData()
    }

    fun onRefreshClicked() {
        updateData()
    }

    fun updateData() = viewModelScope.launch(dispatcher.IO) {
        try {
            _isLoading.emit(true)
            val messages = messageRepository.fetchAllMessages()

            val items = messages.map {
                DatabaseAdapter.Item.DatabaseMessageItem(it.text)
            }

            withContext(dispatcher.Main) {
                adapter.submitList(items)
                _isLoading.emit(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isLoading.emit(false)
        }
    }

    fun onPostMessageClicked(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                messageRepository.postMessage(text)
                updateData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
