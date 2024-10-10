package com.perfomer.checkielite.common.ui.cui.widget.info

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipScope.CuiTooltip(
    text: String,
    icon: Painter = painterResource(R.drawable.ic_info),
) {
    RichTooltip(
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .border(
                width = 1.dp,
                color = LocalCuiPalette.current.OutlineAccentPrimary,
                shape = RoundedCornerShape(16.dp),
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = icon,
                tint = LocalCuiPalette.current.IconAccent,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
                    .offset(y = 4.dp)
            )

            Text(
                text = text,
                fontSize = 14.sp,
                color = LocalCuiPalette.current.TextPrimary,
            )
        }
    }
}