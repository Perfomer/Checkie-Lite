package com.perfomer.checkielite.common.ui.cui.widget.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiOutlineButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = LocalTextStyle.current.color,
    borderColor: Color = LocalCuiPalette.current.OutlineSecondary,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedButton(
        shape = RoundedCornerShape(24.dp),
        onClick = onClick,
        border = BorderStroke(1.dp, borderColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
            .defaultMinSize(minWidth = 128.dp)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(8.dp))
        }

        CuiButtonText(
            text = text,
            textColor = textColor,
            fontSize = 14.sp,
        )

        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            trailingIcon()
        }
    }
}

@WidgetPreview
@Composable
private fun CuiOutlineButtonPreview() = CheckieLiteTheme {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        CuiOutlineButton(
            text = "Next",
            onClick = {}
        )

        CuiOutlineButton(
            text = "Descending",
            textColor = LocalCuiPalette.current.TextPrimary,
            trailingIcon = {
                Icon(
                    painter = painterResource(CommonDrawable.ic_tick),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            onClick = {},
        )
    }
}