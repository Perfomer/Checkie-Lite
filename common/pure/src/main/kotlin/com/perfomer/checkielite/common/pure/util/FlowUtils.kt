package com.perfomer.checkielite.common.pure.util

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
fun <T> flow(block: suspend () -> T): Flow<T> = block.asFlow()

fun <T> Flow<T>.startWith(value: T): Flow<T> = onStart { emit(value) }

fun <T> Flow<T>.onCatchReturn(block: (Throwable) -> T) = catch { error -> emit(block(error)) }