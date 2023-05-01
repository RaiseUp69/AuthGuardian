package com.authguardian.mobileapp.provider

import android.content.Context
import com.authguardian.mobileapp.implementation.DataStoreRepositoryImpl

class DataStoreRepositoryProvider private constructor() {

    private lateinit var dataStoreRepositoryImpl: DataStoreRepositoryImpl

    fun getInstance(context: Context): DataStoreRepositoryImpl {
        if (!::dataStoreRepositoryImpl.isInitialized) {
            dataStoreRepositoryImpl = DataStoreRepositoryImpl(context.applicationContext)
        }
        return dataStoreRepositoryImpl
    }

    companion object {

        val instance: DataStoreRepositoryProvider by lazy { DataStoreRepositoryProvider() }
    }
}