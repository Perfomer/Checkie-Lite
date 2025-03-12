package com.perfomer.checkielite.navigation.result

import com.perfomer.checkielite.core.navigation.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal class NavigationResultEventBus {

    private val results: MutableSharedFlow<Pair<String, Result?>> = MutableSharedFlow()

    fun sendResult(key: String, result: Result) = runBlocking {
        results.emit(key to result)
    }

    fun cancelResult(key: String) = runBlocking {
        results.emit(key to null)
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <R : Result> awaitResult(key: String): R? {
        return results
            .first { (resultKey, _) -> resultKey == key }
            .second as R?
    }
}