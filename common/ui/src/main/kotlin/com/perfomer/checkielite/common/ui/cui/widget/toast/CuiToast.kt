package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
fun CuiToast(
    data: CuiToastData,
    onClick: () -> Unit,
) {
    val actualBackgroundColor = if (data.backgroundColor == Color.Unspecified) {
        LocalCuiPalette.current.BackgroundElevation
    } else {
        data.backgroundColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(top = 40.dp)
            .shadow(
                elevation = LocalCuiPalette.current.LargeElevation,
                spotColor = Color.Black.copy(alpha = 0.7F),
                shape = CircleShape,
            )
            .background(color = actualBackgroundColor)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp)
            .padding(end = 24.dp, start = 16.dp)
    ) {
        if (data.icon != null) {
            Icon(
                painter = data.icon,
                tint = data.iconTint,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = data.message,
            color = LocalCuiPalette.current.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}