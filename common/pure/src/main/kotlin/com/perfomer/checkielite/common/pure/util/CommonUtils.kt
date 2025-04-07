package com.perfomer.checkielite.common.pure.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Calls the specified function [block] on this object if [predicate] returns true
 * and returns its result. Returns null if the predicate is false.
 *
 * @param predicate condition that determines if the block should be executed
 * @param block function to execute if predicate is true
 * @return the result of the block execution or [this] if the predicate is false
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> T.runIf(predicate: Boolean, block: T.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return if (predicate) {
        run(block)
    } else {
        this
    }
}