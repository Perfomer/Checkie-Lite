package com.perfomer.checkielite.common.android

import android.content.Intent
import kotlin.system.exitProcess

interface AppRestarter {
    fun restart()
}

internal class AppRestarterImpl(
    private val singleActivityHolder: SingleActivityHolder,
) : AppRestarter {

    override fun restart() {
        val context = singleActivityHolder.activity
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        context.startActivity(intent)
        exitProcess(0)
    }
}