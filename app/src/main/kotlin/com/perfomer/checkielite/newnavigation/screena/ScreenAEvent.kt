package com.perfomer.checkielite.newnavigation.screena

sealed interface ScreenAEvent {
    data object ClickButtonA: ScreenAEvent
    data class UpdateText(val text: String): ScreenAEvent
}