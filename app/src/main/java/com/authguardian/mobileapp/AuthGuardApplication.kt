package com.authguardian.mobileapp

import android.app.Application
import android.content.Context
import com.authguardian.mobileapp.constant.DIConstant.SERVER_URL_TAG
import com.authguardian.mobileapp.constant.NETWORK.SERVER_URL
import com.authguardian.mobileapp.di.module.adapterModule
import com.authguardian.mobileapp.di.module.coroutineModule
import com.authguardian.mobileapp.di.module.networkModule
import com.authguardian.mobileapp.di.module.viewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton
import org.kodein.di.with

class AuthGuardApplication : Application(), DIAware {

    override val di by DI.lazy {
        constant(tag = SERVER_URL_TAG) with SERVER_URL
        bind<Application>() with singleton { this@AuthGuardApplication }
        bind<Context>() with singleton { this@AuthGuardApplication }  // TODO: Context is singleton or is it a new instance every time?
        import(networkModule)
        import(viewModelModule)
        import(adapterModule)
        import(coroutineModule)
    }
}