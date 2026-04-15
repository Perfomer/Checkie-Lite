package com.perfomer.checkielite.common.pure.util

import java.util.Locale

fun String.splitAtIndex(index: Int): Pair<String, String> {
    return require(index in 0..length).let {
        take(index) to substring(index)
    }
}

fun String.capitalize(locale: Locale = Locale.ROOT): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}