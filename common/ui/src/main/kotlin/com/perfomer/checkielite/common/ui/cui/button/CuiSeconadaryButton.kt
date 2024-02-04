package com.perfomer.checkielite.common.ui.cui.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.Green
import com.perfomer.checkielite.common.ui.theme.GreyDark
import com.perfomer.checkielite.common.ui.theme.GreyLight
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.White
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    activeButtonColor: Color = Green,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = 12.dp,
        pressedElevation = 0.dp,
    ),
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    CuiInternalButton(
        text = text,
        textColor = White,
        textColorDisabled = GreyDark,
        colors = ButtonDefaults.buttonColors(
            containerColor = activeButtonColor,
            disabledContainerColor = GreyLight,
        ),
        elevation = elevation,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
    )
}

@WidgetPreview
@Composable
private fun BuiSecondaryButtonPreview() {
    PreviewTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            CuiSecondaryButton(
                text = "Next",
                onClick = {}
            )

            CuiSecondaryButton(
                text = "Next",
                enabled = false,
                onClick = {},
            )

            CuiSecondaryButton(
                text = "Next",
                loading = true,
                onClick = {},
            )
        }
    }
}