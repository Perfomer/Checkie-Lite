package com.perfomer.checkielite.navigation.decompose

import androidx.activity.compose.BackHandler
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetHost(
    // TODO: Extract styling from navigation module.
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    sheetElevation: Dp = BottomSheetDefaults.Elevation,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    sheetState: ModalBottomSheetState,
    onBackPress: () -> Unit,
    onDismiss: () -> Unit = onBackPress,
    content: @Composable () -> Unit,
) {
    // TODO: Extract the exact bottom sheet implementation from here.
    ModalBottomSheet(
        state = sheetState,
        properties = ModalSheetProperties(dismissOnBackPress = false),
        onDismiss = onDismiss,
    ) {
        val window = LocalModalWindow.current
        LaunchedEffect(Unit) {
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            with(WindowInsetsControllerCompat(window, window.decorView)) {
                isAppearanceLightNavigationBars = true
            }
        }

        // TODO: ClearFocusOnKeyboardClose()

        Scrim(scrimColor = scrimColor, enter = fadeIn(), exit = fadeOut())

        BackHandler(
            enabled = sheetState.currentDetent != SheetDetent.Hidden,
            onBack = onBackPress,
        )

        Sheet(
            modifier = Modifier
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
