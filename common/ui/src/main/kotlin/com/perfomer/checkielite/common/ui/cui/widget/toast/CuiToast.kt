package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.cui.widget.text.WrapTextContent
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiToast(
    data: CuiToastData,
    onClick: () -> Unit,
) {
    val actualBackgroundColor = if (data.backgroundColor == Color.Unspecified) {
        LocalCuiPalette.current.BackgroundElevationContent
    } else {
        data.backgroundColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp)
            .shadow(
                elevation = LocalCuiPalette.current.LargeElevation,
                spotColor = Color.Black.copy(alpha = 0.7F),
                shape = CircleShape,
            )
            .clip(CircleShape)
            .background(color = actualBackgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp)
            .padding(start = 16.dp, end = 24.dp)
    ) {
        if (data.icon != null) {
            Icon(
                painter = data.icon,
                tint = data.iconTint,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            CuiSpacer(16.dp)
        } else {
            CuiSpacer(8.dp)
        }

        WrapTextContent(
            text = data.message,
            color = LocalCuiPalette.current.TextPrimary,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@WidgetPreview
@Composable
private fun CuiToastPreview() = CheckieLiteTheme {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 32.dp)
    ) {
        CuiToast(
            data = CuiToastData(
                message = "Short text",
                icon = null,
                iconTint = Color.Unspecified,
                backgroundColor = LocalCuiPalette.current.BackgroundElevationContent,
                durationMs = 1000L,
            ),
            onClick = {},
        )

        CuiToast(
            data = CuiToastData(
                message = "Short text",
                icon = painterResource(CommonDrawable.ic_error),
                iconTint = LocalCuiPalette.current.IconNegative,
                backgroundColor = LocalCuiPalette.current.BackgroundNegativeSecondary,
                durationMs = 1000L,
            ),
            onClick = {},
        )

        CuiToast(
            data = CuiToastData(
                message = "Failed to export backup: Not enough space",
                icon = painterResource(CommonDrawable.ic_warning),
                iconTint = LocalCuiPalette.current.IconWarning,
                backgroundColor = LocalCuiPalette.current.BackgroundWarningSecondary,
                durationMs = 1000L,
            ),
            onClick = {},
        )
    }
}