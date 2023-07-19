package com.authguardian.mobileapp

import android.app.Application
import com.authguardian.mobileapp.adapter.DatabaseAdapter
import com.authguardian.mobileapp.entity.MessageResponse
import com.authguardian.mobileapp.service.MessageService
import com.authguardian.mobileapp.utils.TestCoroutineDispatchersProvider
import com.authguardian.mobileapp.viewmodel.DatabaseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class DatabaseViewModelTest {

    // Mockks
    private val application = mockk<Application>(relaxed = true)
    private val adapter = mockk<DatabaseAdapter>(relaxed = true)
    private val messageService = mockk<MessageService>()

    private val viewModel = DatabaseViewModel(application, adapter, messageService, TestCoroutineDispatchersProvider())

    @Test
    fun `Given messages returned by server, when updateData is called, then should verify that getMessages was called and submit messages to adapter`() = runTest {
        // Given
        val messages = listOf(
            MessageResponse("1", "message1"),
            MessageResponse("2", "message2"),
            MessageResponse("3", "message3")
        )
        val expectedItems = messages.map { DatabaseAdapter.Item.DatabaseMessageItem(it.text) }
        coEvery { messageService.getMessages() } returns messages

        // When
        viewModel.updateData()

        // Then
        coVerify {
            messageService.getMessages()
            adapter.submitList(expectedItems)
        }
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `When updateData is called without any errors, then should set isLoading to false`() = runTest {
        // Given
        val messages = listOf(
            MessageResponse("1", "message1"),
            MessageResponse("2", "message2"),
            MessageResponse("3", "message3")
        )
        coEvery { messageService.getMessages() } returns messages

        // When
        viewModel.updateData()

        // Then
        assertEquals(false, viewModel.isLoading.value)
    }
}
