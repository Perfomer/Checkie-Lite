package com.perfomer.checkielite.common.tea.exception

fun interface UnhandledExceptionHandler {
    fun handle(throwable: Throwable)
}