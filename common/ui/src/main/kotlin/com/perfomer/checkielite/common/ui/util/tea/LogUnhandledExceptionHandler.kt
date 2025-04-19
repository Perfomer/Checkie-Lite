package com.perfomer.checkielite.common.ui.util.tea

import android.util.Log
import com.perfomer.checkielite.common.pure.appInfo.AppInfo
import com.perfomer.checkielite.common.tea.exception.UnhandledExceptionHandler

class LogUnhandledExceptionHandler(
    private val tag: String,
) : UnhandledExceptionHandler {

    override fun handle(throwable: Throwable) {
        if (AppInfo.isDebug) {
            // For debug variant we want to notice crashes as soon as possible
            throw throwable
        } else {
            Log.e(tag, "Unhandled exception!", throwable)
        }
    }
}