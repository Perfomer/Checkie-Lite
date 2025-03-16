package com.perfomer.checkielite.core.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable

interface NavigationHost {

    context(ComponentActivity)
    fun initialize(startDestination: Destination)

    fun back()

    @Composable
    fun NavigationRoot(
        bottomSheetController: BottomSheetController,
        bottomSheetContent: @Composable (@Composable () -> Unit) -> Unit,
        overlayContent: @Composable (@Composable () -> Unit) -> Unit,
    )
}