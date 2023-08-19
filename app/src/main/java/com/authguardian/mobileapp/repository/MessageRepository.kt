package com.authguardian.mobileapp.repository

import com.authguardian.mobileapp.request.MessageRequest
import com.authguardian.mobileapp.service.MessageService

class MessageRepository(private val messageService: MessageService) {

    suspend fun postMessage(message: String) {
        messageService.postMessage(MessageRequest(message))
    }
}