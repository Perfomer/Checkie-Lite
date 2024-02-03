package com.perfomer.checkielite.navigation

import androidx.compose.runtime.Composable

class StartStackScreenProvider(
    private val singleStackScreenContent: SingleStackScreenContent,
) {

    @Composable
    fun Content() {
        return singleStackScreenContent.Content()
    }
}