package com.perfomer.checkielite.navigation.voyager.impl

import com.perfomer.checkielite.common.android.SingleActivityHolder

internal interface ApplicationExitProvider {

    fun exit()
}

internal class AndroidApplicationExitProvider(
    private val activityHolder: SingleActivityHolder,
) : ApplicationExitProvider {

    override fun exit() {
        activityHolder.activity.moveTaskToBack(true)
    }
}