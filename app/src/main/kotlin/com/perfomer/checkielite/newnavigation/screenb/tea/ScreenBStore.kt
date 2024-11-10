package com.perfomer.checkielite.newnavigation.screenb.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.newnavigation.BDestination

class ScreenBStore(
    componentContext: ComponentContext,
    params: BDestination,
) : ComponentStore<ScreenBCommand, ScreenBEffect, ScreenBEvent, ScreenBUiEvent, ScreenBState, ScreenBState>(
    componentContext = componentContext,
    initialState = ScreenBState(params.text),
    reducer = ScreenBReducer(),
    actors = emptySet(),
)

data class ScreenBState(
    val text: String,
)

sealed interface ScreenBCommand {
    data object Exit : ScreenBCommand
}

sealed interface ScreenBEvent {
}

sealed interface ScreenBUiEvent : ScreenBEvent {
    data object OnBackClick : ScreenBUiEvent
    data object OnShowToastClick : ScreenBUiEvent
}

sealed interface ScreenBEffect {
    data object ShowToast : ScreenBEffect
}