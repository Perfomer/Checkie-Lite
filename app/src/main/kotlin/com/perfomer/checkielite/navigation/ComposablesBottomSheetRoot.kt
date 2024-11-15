package com.perfomer.checkielite.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowInsetsControllerCompat
import com.composables.core.LocalModalWindow
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.ModalSheetProperties
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.core.navigation.BottomSheetController
import android.graphics.Color as AndroidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComposablesBottomSheetRoot(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(SheetDetent.Hidden),
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    sheetElevation: Dp = BottomSheetDefaults.Elevation,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        state = sheetState,
        onDismiss = onDismiss,
        properties = ModalSheetProperties(dismissOnBackPress = false)
    ) {
        val window = LocalModalWindow.current
        LaunchedEffect(Unit) {
            window.navigationBarColor = AndroidColor.TRANSPARENT
            with(WindowInsetsControllerCompat(window, window.decorView)) {
                isAppearanceLightNavigationBars = true
            }
        }

        ClearFocusOnKeyboardClose()

        Scrim(scrimColor = scrimColor, enter = fadeIn(), exit = fadeOut())

        Sheet(
            modifier = modifier
                .statusBarsPadding()
                .shadow(sheetElevation, shape)
                .clip(shape)
                .background(containerColor)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            ) {
                dragHandle?.invoke()
                content()
            }
        }
    }
}

internal class ComposablesBottomSheetController(
    private val sheetState: ModalBottomSheetState,
) : BottomSheetController {

    override val isVisible: Boolean
        get() = sheetState.targetDetent != SheetDetent.Hidden

    override suspend fun show() {
        sheetState.animateTo(SheetDetent.FullyExpanded)
    }

    override suspend fun hide() {
        sheetState.animateTo(SheetDetent.Hidden)
    }
}
