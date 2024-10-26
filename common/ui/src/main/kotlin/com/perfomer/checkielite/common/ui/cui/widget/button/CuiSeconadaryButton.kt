package com.perfomer.checkielite.common.ui.cui.widget.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = LocalCuiPalette.current.TextPrimary,
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    CuiInternalButton(
        text = text,
        textColor = textColor,
        textColorDisabled = LocalCuiPalette.current.TextSecondary,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            contentColor = textColor,
            disabledContentColor = LocalCuiPalette.current.TextSecondary,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = LocalCuiPalette.current.OutlineSecondary,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
        ),
        enabled = enabled,
        loading = loading,
        onClick = onClick,
        modifier = modifier,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
    )
}

@WidgetPreview
@Composable
private fun CuiSecondaryButtonPreview() = CheckieLiteTheme {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        CuiSecondaryButton(
            text = "Next",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )

        CuiSecondaryButton(
            text = "Next",
            enabled = false,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )

        CuiSecondaryButton(
            text = "Next",
            loading = true,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}