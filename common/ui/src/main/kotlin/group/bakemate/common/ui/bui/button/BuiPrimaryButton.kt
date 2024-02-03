package group.bakemate.common.ui.bui.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import group.bakemate.common.ui.theme.GreyDark
import group.bakemate.common.ui.theme.GreyLight
import group.bakemate.common.ui.theme.Orange
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.White
import group.bakemate.common.ui.theme.WidgetPreview

@Composable
fun BuiPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    activeButtonColor: Color = Orange,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = 12.dp,
        pressedElevation = 0.dp,
    ),
    isEnabled: Boolean = true,
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
        enabled = isEnabled,
        loading = loading,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
    )
}

@WidgetPreview
@Composable
private fun BuiPrimaryButtonPreview() {
    PreviewTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            BuiPrimaryButton(
                text = "Next",
                onClick = {}
            )

            BuiPrimaryButton(
                text = "Next",
                isEnabled = false,
                onClick = {},
            )

            BuiPrimaryButton(
                text = "Next",
                loading = true,
                onClick = {},
            )
        }
    }
}