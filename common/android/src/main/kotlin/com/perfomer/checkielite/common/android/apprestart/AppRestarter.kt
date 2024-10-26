package com.perfomer.checkielite.common.android.apprestart

import android.app.Activity
import android.content.Intent
import com.perfomer.checkielite.common.android.SingleActivityHolder
import kotlin.system.exitProcess

interface AppRestarter {

    fun restart(vararg actions: RestartAction)

    context(Activity)
    fun extractStartActions(): List<RestartAction>
}

internal class AppRestarterImpl(
    private val singleActivityHolder: SingleActivityHolder,
) : AppRestarter {

    override fun restart(vararg actions: RestartAction) {
        val context = singleActivityHolder.activity
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(EXTRA_START_ACTIONS, ArrayList(actions.toList()))
        }

        context.startActivity(intent)
        exitProcess(0)
    }

    context(Activity)
    @Suppress("DEPRECATION")
    override fun extractStartActions(): List<RestartAction> {
        val startActions = intent.getParcelableArrayListExtra<RestartAction>(EXTRA_START_ACTIONS)
            .orEmpty()

        // Clear start actions to not handle it again after configuration change
        intent.replaceExtras(null)

        return startActions

    }

    private companion object {
        private const val EXTRA_START_ACTIONS = "extra_start_actions"
    }
}