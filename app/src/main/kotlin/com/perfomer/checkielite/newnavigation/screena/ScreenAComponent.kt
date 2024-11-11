package com.perfomer.checkielite.newnavigation.screena

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.newnavigation.BDestination

class ScreenAComponent(
    componentContext: ComponentContext,
    private val router: Router,
) : ComponentContext by componentContext {

    private var _text = MutableValue("")
    val text: Value<String> = _text

    fun onEvent(event: ScreenAEvent) {
        when (event) {
            ScreenAEvent.ClickButtonA -> {
                router.navigate(BDestination(text.value))
            }
            is ScreenAEvent.UpdateText -> {
                _text.value = event.text
            }
        }
    }
}