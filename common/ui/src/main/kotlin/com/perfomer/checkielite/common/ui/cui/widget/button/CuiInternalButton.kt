package com.perfomer.checkielite.common.ui.cui.widget.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
internal fun CuiInternalButton(
    textColor: Color,
    textColorDisabled: Color,
    colors: ButtonColors,
    elevation: ButtonElevation,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    val actualEnabled = remember(enabled, loading) { enabled && !loading }
    val actualTextColor = if (actualEnabled) textColor else textColorDisabled

    Button(
        shape = RoundedCornerShape(24.dp),
        colors = colors,
        elevation = elevation,
        enabled = actualEnabled,
        onClick = onClick,
        border = border,
        modifier = modifier
            .height(56.dp)
    ) {
        if (loading) {
            CuiButtonLoader()
        } else {
            if (leadingIcon != null) {
                leadingIcon()
                CuiSpacer(8.dp)
            }

            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = actualTextColor,
                ),
                content = content,
            )

            if (trailingIcon != null) {
                CuiSpacer(8.dp)
                trailingIcon()
            }
        }
    }
}

@WidgetPreview
@Composable
private fun CuiButtonLoader() {
    CircularProgressIndicator(
        color = LocalCuiPalette.current.BackgroundPositivePrimary,
        strokeWidth = 2.dp,
        modifier = Modifier.size(24.dp),
    )
}
