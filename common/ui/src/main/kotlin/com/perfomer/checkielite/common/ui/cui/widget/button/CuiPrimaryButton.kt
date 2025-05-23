package com.perfomer.checkielite.common.ui.cui.widget.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    activeButtonColor: Color = LocalCuiPalette.current.BackgroundAccentPrimary,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = LocalCuiPalette.current.MediumElevation,
        pressedElevation = 0.dp,
    ),
    isEnabled: Boolean = true,
    loading: Boolean = false,
    borderStroke: BorderStroke? = null,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    CuiPrimaryButton(
        activeButtonColor = activeButtonColor,
        elevation = elevation,
        isEnabled = isEnabled,
        borderStroke = borderStroke,
        loading = loading,
        onClick = onClick,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier
    ) {
        Text(text)
    }
}

@Composable
fun CuiPrimaryButton(
    modifier: Modifier = Modifier,
    activeButtonColor: Color = LocalCuiPalette.current.BackgroundAccentPrimary,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = LocalCuiPalette.current.MediumElevation,
        pressedElevation = 0.dp,
    ),
    isEnabled: Boolean = true,
    loading: Boolean = false,
    borderStroke: BorderStroke? = null,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    CuiInternalButton(
        textColor = LocalCuiPalette.current.TextInverted,
        textColorDisabled = LocalCuiPalette.current.TextSecondary,
        colors = ButtonDefaults.buttonColors(
            containerColor = activeButtonColor,
            disabledContainerColor = LocalCuiPalette.current.BackgroundSecondary,
        ),
        border = borderStroke,
        elevation = elevation,
        enabled = isEnabled,
        loading = loading,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        content = content,
    )
}

@WidgetPreview
@Composable
private fun CuiPrimaryButtonPreview() = CheckieLiteTheme {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        CuiPrimaryButton(
            text = "Next",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )

        CuiPrimaryButton(
            text = "Next",
            isEnabled = false,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )

        CuiPrimaryButton(
            text = "Next",
            loading = true,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}