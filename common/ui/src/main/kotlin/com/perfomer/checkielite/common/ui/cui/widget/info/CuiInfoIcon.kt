package com.perfomer.checkielite.common.ui.cui.widget.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuiInfoIcon(
    text: String,
    modifier: Modifier = Modifier
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(
            spacingBetweenTooltipAndAnchor = 12.dp,
        ),
        tooltip = { CuiTooltip(text = text) },
        state = tooltipState,
        modifier = modifier,
    ) {
        InfoIcon(onClick = { scope.launch { tooltipState.show() } })
    }
}

@Composable
internal fun InfoIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = LocalCuiPalette.current.OutlineSecondary,
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .background(
                color = LocalCuiPalette.current.BackgroundSecondary,
                shape = CircleShape
            )
            .clip(RoundedCornerShape(14.dp))
            .size(24.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCuiPalette.current.TextPrimary,
        )
    }
}