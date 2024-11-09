package com.perfomer.checkielite.newnavigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.perfomer.checkielite.core.navigation.api.Params

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Params>()
}