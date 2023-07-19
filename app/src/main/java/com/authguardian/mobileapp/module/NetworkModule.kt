package com.authguardian.mobileapp.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
class NetworkModule {

    fun getRetrofitInstance(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.118:8080/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun getOkHttpClientInstance(context: Context) = OkHttpClient.Builder().apply {
        connectTimeout(1000, TimeUnit.SECONDS)
        readTimeout(1000, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(context))
    }.build()
}