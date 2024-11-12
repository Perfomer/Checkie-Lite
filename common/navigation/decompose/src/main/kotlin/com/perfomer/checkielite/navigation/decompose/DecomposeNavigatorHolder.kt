package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.perfomer.checkielite.core.navigation.Destination

internal class DecomposeNavigatorHolder(
    val mainNavigator: StackNavigation<Destination> = StackNavigation(),
    val bottomSheetNavigator: SlotNavigation<Destination> = SlotNavigation(),
    val overlayNavigator: SlotNavigation<Destination> = SlotNavigation(),
)
