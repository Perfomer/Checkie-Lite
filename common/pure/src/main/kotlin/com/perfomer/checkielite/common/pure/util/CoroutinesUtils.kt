package com.perfomer.checkielite.common.pure.util

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.resume

suspend fun <T, R> Iterable<T>.mapAsync(
    transformation: suspend (T) -> R
): List<R> = coroutineScope {
    this@mapAsync
        .map { async { transformation(it) } }
        .awaitAll()
}

suspend fun <T> Iterable<T>.forEachAsync(
    block: suspend (T) -> Unit
): Unit = coroutineScope {
    this@forEachAsync
        .map { async { block(it) } }
        .awaitAll()
}

fun <T> CancellableContinuation<T>.safeResume(value: T) {
    if (isActive) resume(value)
}