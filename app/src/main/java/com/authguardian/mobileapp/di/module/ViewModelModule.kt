package com.authguardian.mobileapp.di.module

import com.authguardian.mobileapp.constant.ModulesConstant.VIEW_MODEL_MODULE
import com.authguardian.mobileapp.viewmodel.AuthorizationViewModel
import com.authguardian.mobileapp.viewmodel.DatabaseViewModel
import com.authguardian.mobileapp.viewmodel.GoogleSignInSuccessfulViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val viewModelModule = DI.Module(VIEW_MODEL_MODULE) {

    bindProvider {
        GoogleSignInSuccessfulViewModel(
            instance()
        )
    }

    bindProvider {
        DatabaseViewModel(
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bindProvider {
        AuthorizationViewModel(
            instance()
        )
    }
}
