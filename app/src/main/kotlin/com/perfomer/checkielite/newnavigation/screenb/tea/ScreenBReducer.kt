package com.perfomer.checkielite.newnavigation.screenb.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer

class ScreenBReducer : DslReducer<ScreenBCommand, ScreenBEffect, ScreenBEvent, ScreenBState>() {

    override fun reduce(event: ScreenBEvent) = when (event) {
        ScreenBUiEvent.OnBackClick -> commands(ScreenBCommand.Exit)
        ScreenBUiEvent.OnShowToastClick -> effects(ScreenBEffect.ShowToast)
    }
}