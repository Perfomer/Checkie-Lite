package com.perfomer.checkielite.newnavigation.screena

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.perfomer.checkielite.newnavigation.BDestination
import com.perfomer.checkielite.newnavigation.Destination

class ScreenAComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<Destination>
): ComponentContext by componentContext {

    private var _text = MutableValue("")
    val text: Value<String> = _text

    fun onEvent(event: ScreenAEvent) {
        when(event) {
            ScreenAEvent.ClickButtonA -> {
                navigation.pushNew(BDestination(text.value))
            }
            is ScreenAEvent.UpdateText -> {
                _text.value = event.text
            }
        }
    }
}