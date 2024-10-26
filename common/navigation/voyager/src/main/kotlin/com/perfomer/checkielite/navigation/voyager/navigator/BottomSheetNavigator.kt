package com.perfomer.checkielite.navigation.voyager.navigator

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack

interface BottomSheetNavigator : Stack<Screen> {

    val isVisible: Boolean

    fun show(screen: Screen)

    fun hide()
}