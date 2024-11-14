package com.perfomer.checkielite.newnavigation.screena

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.perfomer.checkielite.core.navigation.DestinationMode
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.newnavigation.BDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScreenAComponent(
    componentContext: ComponentContext,
    private val router: Router,
) : ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private var _text = MutableValue("")
    val text: Value<String> = _text

    fun onEvent(event: ScreenAEvent) {
        when (event) {
            ScreenAEvent.ClickButtonA -> {
                scope.launch {
                    val result = router.navigateForResult<String>(
                        BDestination(text.value),
                        DestinationMode.BOTTOM_SHEET,
                    )

                    _text.value = result
                }
//                router.navigate(BDestination(text.value))
            }
            is ScreenAEvent.UpdateText -> {
                _text.value = event.text
            }
        }
    }
}