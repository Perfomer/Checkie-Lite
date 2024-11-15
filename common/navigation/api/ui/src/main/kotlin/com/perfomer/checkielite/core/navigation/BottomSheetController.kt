package com.perfomer.checkielite.core.navigation

interface BottomSheetController {

    val isVisible: Boolean

    suspend fun show()

    suspend fun hide()
}