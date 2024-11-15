package com.perfomer.checkielite.core.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable

interface NavigationHost {

    context(ComponentActivity)
    fun initialize(startDestination: Destination)

    fun hideBottomSheet()

    @Composable
    fun Root(
        bottomSheetController: BottomSheetController,
        bottomSheetContent: @Composable (@Composable () -> Unit) -> Unit,
    )
}