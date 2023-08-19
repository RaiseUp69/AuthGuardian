package com.authguardian.mobileapp.di.module

import com.authguardian.mobileapp.constant.ModulesConstant.COROUTINE_MODULE
import com.authguardian.mobileapp.utils.CoroutineDispatchersProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider

val coroutineModule = DI.Module(COROUTINE_MODULE) {
    bind<CoroutineDispatchersProvider>() with provider { CoroutineDispatchersProvider() }
}