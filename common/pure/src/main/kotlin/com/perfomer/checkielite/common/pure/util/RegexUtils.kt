package com.perfomer.checkielite.common.pure.util

fun Set<Char>.toRegex(): Regex {
    return Regex(
        this.joinToString(
            separator = "",
            prefix = "[",
            postfix = "]"
        ) { c -> """\$c""" }
    )
}