package com.perfomer.checkielite.navigation

import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.perfomer.checkielite.core.navigation.BottomSheetController

internal class ComposablesBottomSheetController(
    private val sheetState: ModalBottomSheetState,
) : BottomSheetController {

    override val isVisible: Boolean
        get() = sheetState.targetDetent != SheetDetent.Hidden

    // Needed to avoid duplicated `router.back()` call
    internal var shouldIgnoreDismiss: Boolean = false
        private set

    override suspend fun show() {
        shouldIgnoreDismiss = false
        sheetState.animateTo(SheetDetent.FullyExpanded)
    }

    override suspend fun hide() {
        shouldIgnoreDismiss = true
        sheetState.animateTo(SheetDetent.Hidden)
    }
}
