package com.perfomer.checkielite.common.ui.util.tea

import android.util.Log
import com.perfomer.checkielite.common.tea.exception.UnhandledExceptionHandler

class LogUnhandledExceptionHandler(
    private val tag: String,
) : UnhandledExceptionHandler {

    override fun handle(throwable: Throwable) {
        Log.e(tag, "Unhandled exception!", throwable)
    }
}