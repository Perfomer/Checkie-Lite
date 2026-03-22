package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.modifier.thenIfNotNull

@Composable
internal fun TagsStatPill(
    text: String,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    trailingPainter: Painter? = null,
    onClick: (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(color = backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = CircleShape)
            .thenIfNotNull(onClick) { onClick -> clickable(onClick = onClick) }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
            )

            if (trailingPainter != null) {
                Icon(
                    painter = trailingPainter,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(12.dp)
                )
            }
        }
    }
}
