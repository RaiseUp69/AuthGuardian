package com.authguardian.mobileapp.utils

import com.authguardian.mobileapp.module.NetworkModule
import com.authguardian.mobileapp.service.MessageService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Network {

    private val moshiInstance: Moshi = Moshi.Builder().build()
    fun getRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = NetworkModule().getRetrofitInstance(moshi = moshiInstance, okHttpClient = okHttpClient)
    fun getMessageServiceInstance(retrofit: Retrofit): MessageService = retrofit.create(MessageService::class.java)
}