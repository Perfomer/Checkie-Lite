package com.perfomer.checkielite.common.ui.cui.widget.bottomsheet

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.ModalSheetProperties
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.perfomer.checkielite.common.ui.cui.widget.sheet.CuiDragAnchor
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.common.ui.util.DialogTransparentNavBar
import com.perfomer.checkielite.common.ui.util.navigation.PredictiveBackHandler

@Composable
fun CuiBottomSheet(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(SheetDetent.Hidden),
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    BaseBottomSheet(
        sheetState = sheetState,
        sheetElevation = LocalCuiPalette.current.LargeElevation,
        containerColor = LocalCuiPalette.current.BackgroundPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { CuiDragAnchor() },
        onDismiss = onDismiss,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BaseBottomSheet(
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
    var backProgress by remember { mutableFloatStateOf(0F) }

    LaunchedEffect(sheetState.isIdle) {
        if (sheetState.targetDetent == SheetDetent.Hidden && sheetState.isIdle) {
            backProgress = 0F
        }
    }

    ModalBottomSheet(
        state = sheetState,
        onDismiss = onDismiss,
        properties = ModalSheetProperties(dismissOnBackPress = false),
    ) {
        PredictiveBackHandler(
            enabled = sheetState.targetDetent != SheetDetent.Hidden,
            onBack = onDismiss,
            onProgress = { backProgress = it },
        )

        DialogTransparentNavBar()
        ClearFocusOnKeyboardClose()

        Scrim(scrimColor = scrimColor, enter = fadeIn(), exit = fadeOut())

        Sheet(
            modifier = modifier
                .graphicsLayer {
                    transformOrigin = TransformOrigin(pivotFractionX = 0.5F, pivotFractionY = 1.0F)
                    translationY = backProgress * 72.dp.toPx()
                    scaleX = 1 - backProgress * 0.05F
                    scaleY = 1 - backProgress * 0.05F
                }
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