package com.perfomer.checkielite.common.ui.cui.widget.dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.Scrim
import com.composables.core.rememberDialogState
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.common.ui.util.DialogTransparentNavBar
import com.perfomer.checkielite.common.ui.util.navigation.PredictiveBackHandler

private const val ANIMATION_DURATION_MS = 150

@Composable
fun CuiAlertDialog(
    isVisible: Boolean,
    title: String,
    text: String,
    confirmText: String = stringResource(CommonString.common_ok),
    confirmColor: Color = LocalCuiPalette.current.TextAccent,
    onConfirm: () -> Unit,
    dismissText: String? = stringResource(CommonString.common_cancel),
    dismissColor: Color = LocalCuiPalette.current.TextAccent,
    onDismiss: () -> Unit,
) {
    CuiAlertDialogInternal(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        CuiAlertDialogContent(
            title = title,
            text = { Text(text) },
            confirmText = confirmText,
            confirmColor = confirmColor,
            onConfirm = onConfirm,
            dismissText = dismissText,
            dismissColor = dismissColor,
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun CuiAlertDialog(
    isVisible: Boolean,
    title: String,
    text: AnnotatedString,
    confirmText: String = stringResource(CommonString.common_ok),
    confirmColor: Color = LocalCuiPalette.current.TextAccent,
    onConfirm: () -> Unit,
    dismissText: String? = stringResource(CommonString.common_cancel),
    dismissColor: Color = LocalCuiPalette.current.TextAccent,
    onDismiss: () -> Unit,
) {
    CuiAlertDialogInternal(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        CuiAlertDialogContent(
            title = title,
            text = { Text(text) },
            confirmText = confirmText,
            confirmColor = confirmColor,
            onConfirm = onConfirm,
            dismissText = dismissText,
            dismissColor = dismissColor,
            onDismiss = onDismiss,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CuiAlertDialogInternal(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    var backProgress by remember { mutableFloatStateOf(0F) }
    val animatedBackProgress by animateFloatAsState(backProgress, label = "DialogBackProgressSmooth")

    val dialogState = rememberDialogState(initiallyVisible = isVisible)

    UpdateEffect(isVisible) {
        dialogState.visible = isVisible
        if (!isVisible) backProgress = 0F
    }

    Dialog(
        state = dialogState,
        onDismiss = onDismiss,
    ) {
        PredictiveBackHandler(
            enabled = dialogState.visible,
            onBack = onDismiss,
            onProgress = { backProgress = it },
        )

        DialogTransparentNavBar()

        Scrim(scrimColor = BottomSheetDefaults.ScrimColor, enter = fadeIn(), exit = fadeOut())

        DialogPanel(
            enter = scaleIn(tween(durationMillis = ANIMATION_DURATION_MS), 0.8F) + fadeIn(tween(durationMillis = ANIMATION_DURATION_MS)),
            exit = scaleOut(tween(durationMillis = ANIMATION_DURATION_MS), 0.6F) + fadeOut(tween(durationMillis = ANIMATION_DURATION_MS)),
            content = content,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = 1 - animatedBackProgress * 0.2F
                    scaleY = 1 - animatedBackProgress * 0.2F
                }
                .systemBarsPadding()
                .widthIn(min = 280.dp, max = 560.dp)
                .padding(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(LocalCuiPalette.current.BackgroundElevationBase)
        )
    }
}

@Composable
private fun CuiAlertDialogContent(
    title: String,
    text: @Composable () -> Unit,
    confirmText: String,
    confirmColor: Color,
    onConfirm: () -> Unit,
    dismissText: String?,
    dismissColor: Color,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )

            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    color = LocalCuiPalette.current.TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                ),
                content = text,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.End),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 20.dp)
        ) {
            dismissText?.let {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = dismissText,
                        color = dismissColor,
                    )
                }
            }

            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
            ) {
                Text(
                    text = confirmText,
                    color = confirmColor,
                )
            }
        }
    }
}

@WidgetPreview
@Composable
private fun CuiAlertDialogPreview() = CheckieLiteTheme {
    CuiAlertDialog(
        isVisible = true,
        title = "Oops!..",
        text = "Something went wrong",
        confirmText = "Exit",
        dismissText = "Cancel",
        confirmColor = LocalCuiPalette.current.TextNegative,
        onConfirm = { },
        onDismiss = {},
    )
}