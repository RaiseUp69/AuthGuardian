package com.authguardian.mobileapp.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatchersProvider(
    dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
) : CoroutineDispatchersProvider() {

    override val Default: CoroutineDispatcher = dispatcher
    override val Main: CoroutineDispatcher = dispatcher
    override val IO: CoroutineDispatcher = dispatcher
}