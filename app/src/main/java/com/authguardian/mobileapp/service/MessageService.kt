package com.authguardian.mobileapp.service

import com.authguardian.mobileapp.entity.MessageResponse
import com.authguardian.mobileapp.request.MessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {

    @GET("/")
    suspend fun getMessages(): List<MessageResponse>

    @GET("/{id}")
    suspend fun getMessageById(@Path("id") id: String): MessageResponse

    @POST("/")
    suspend fun postMessage(@Body message: MessageRequest)
}