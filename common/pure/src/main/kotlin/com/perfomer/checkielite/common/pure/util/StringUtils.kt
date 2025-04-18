package com.perfomer.checkielite.common.pure.util

fun String.splitAtIndex(index: Int): Pair<String, String> {
    return require(index in 0..length).let {
        take(index) to substring(index)
    }
}