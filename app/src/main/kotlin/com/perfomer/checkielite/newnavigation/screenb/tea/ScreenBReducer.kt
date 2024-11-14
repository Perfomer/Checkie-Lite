package com.perfomer.checkielite.newnavigation.screenb.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.navigation.Router

class ScreenBReducer(
    private val router: Router,
) : DslReducer<ScreenBCommand, ScreenBEffect, ScreenBEvent, ScreenBState>() {

    override fun reduce(event: ScreenBEvent) = when (event) {
        ScreenBUiEvent.OnBackClick -> router.exitWithResult("Result")
        ScreenBUiEvent.OnShowToastClick -> effects(ScreenBEffect.ShowToast)
    }
}