package com.perfomer.checkielite.common.pure.util

import com.perfomer.checkielite.common.pure.state.Lce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest

fun <T> flowBy(block: suspend () -> T): Flow<T> = block.asFlow()

fun <T> Flow<T>.startWith(value: T): Flow<T> = onStart { emit(value) }

fun <T> Flow<T>.onCatchReturn(block: (Throwable) -> T) = catch { error -> emit(block(error)) }

fun <T> Flow<T>.ignoreResult() = map { null }.filterNotNull()

/**
 * Use it for "rule 300/500".
 *
 * If operations completes faster than [noLoadingDurationMs], then no loading will be shown.
 * If operations completes slower than [noLoadingDurationMs], then loading will be shown at least [minLoadingDurationMs].
 *
 * @param noLoadingDurationMs Duration while we don't want to see any loader
 * @param minLoadingDurationMs Minimum duration of loading if it shown
 * @param defineLoading Define loading value
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.cleanLoading(
    noLoadingDurationMs: Long = 300L,
    minLoadingDurationMs: Long = 500L,
    defineLoading: (T) -> Boolean,
): Flow<T> {
    fun now() = System.currentTimeMillis()

    var expectedLoadingDisappearTimestamp = 0L

    return transformLatest { value ->
        if (defineLoading(value)) {
            // Just wait until we want show loading
            delay(noLoadingDurationMs)

            // If delay wasn't cancelled by new value (because of transform LATEST),
            // Then loader will be shown and we have to decide when to hide it.
            expectedLoadingDisappearTimestamp = now() + minLoadingDurationMs
        } else if (expectedLoadingDisappearTimestamp != 0L) {
            // If we're here, then we received non-loading value.
            // Defining how much time we should wait until hide loader.
            val remainedLoadingDuration = expectedLoadingDisappearTimestamp - now()
            if (remainedLoadingDuration > 0L) {
                delay(remainedLoadingDuration)
            }

            expectedLoadingDisappearTimestamp = 0L
        }

        emit(value)
    }
}

fun <T> Flow<T>.lce(): Flow<Lce<T>> {
    return this
        .map { Lce.Content(it) }
        .onCatchReturn { Lce.Error(it) }
        .startWith(Lce.Loading())
}