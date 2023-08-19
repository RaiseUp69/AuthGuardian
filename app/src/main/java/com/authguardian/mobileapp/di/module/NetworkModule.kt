package com.authguardian.mobileapp.di.module

import com.authguardian.mobileapp.constant.DIConstant.SERVER_URL_TAG
import com.authguardian.mobileapp.constant.ModulesConstant.NETWORK_MODULE
import com.authguardian.mobileapp.repository.MessageRepository
import com.authguardian.mobileapp.service.MessageService
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = DI.Module(NETWORK_MODULE) {

    bind<Moshi>() with singleton {
        Moshi.Builder().build()
    }

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(instance()))
            .build()
    }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .baseUrl(instance<String>(tag = SERVER_URL_TAG))
            .client(instance())
            .addConverterFactory(MoshiConverterFactory.create(instance()))
            .build()
    }

    bind<MessageService>() with singleton {
        instance<Retrofit>().create(MessageService::class.java)
    }

    bind<MessageRepository>() with singleton {
        MessageRepository(instance())
    }
}