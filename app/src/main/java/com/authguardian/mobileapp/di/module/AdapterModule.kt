package com.authguardian.mobileapp.di.module

import com.authguardian.mobileapp.adapter.DatabaseAdapter
import com.authguardian.mobileapp.constant.ModulesConstant.ADAPTER_MODULE
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider

val adapterModule = DI.Module(ADAPTER_MODULE) {
    bind<DatabaseAdapter>() with provider { DatabaseAdapter() }
}