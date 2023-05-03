package com.authguardian.mobileapp.provider

import android.content.Context
import com.authguardian.mobileapp.repository.DataStoreRepository

class DataStoreRepositoryProvider private constructor() {

    private lateinit var dataStoreRepository: DataStoreRepository

    fun getInstance(context: Context): DataStoreRepository {
        if (!::dataStoreRepository.isInitialized) {
            dataStoreRepository = DataStoreRepository(context.applicationContext)
        }
        return dataStoreRepository
    }

    companion object {

        val instance: DataStoreRepositoryProvider by lazy { DataStoreRepositoryProvider() }
    }
}