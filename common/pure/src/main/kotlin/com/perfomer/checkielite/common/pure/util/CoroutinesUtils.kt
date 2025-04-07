package com.perfomer.checkielite.common.pure.util

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

fun <T> CancellableContinuation<T>.safeResumeWithException(error: Throwable) {
    if (isActive) resumeWithException(error)
}

inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (t: TimeoutCancellationException) {
        Result.failure(t)
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <T> CoroutineScope.tryLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onSuccess: (suspend CoroutineScope.(T) -> Unit)? = null,
    onCancel: (suspend CoroutineScope.() -> Unit)? = null,
    onError: (suspend CoroutineScope.(Throwable) -> Unit)? = null,
    finally: (suspend CoroutineScope.() -> Unit)? = null,
    block: suspend CoroutineScope.() -> T,
): Job {
    return launch(context, start) {
        try {
            val result = block()
            onSuccess?.invoke(this, result)
        } catch (error: TimeoutCancellationException) {
            onError?.invoke(this, error) ?: throw error
        } catch (error: CancellationException) {
            withContext(NonCancellable) { onCancel?.invoke(this) }
            throw error
        } catch (error: Throwable) {
            onError?.invoke(this, error) ?: throw error
        } finally {
            withContext(NonCancellable) { finally?.invoke(this) }
        }
    }
}