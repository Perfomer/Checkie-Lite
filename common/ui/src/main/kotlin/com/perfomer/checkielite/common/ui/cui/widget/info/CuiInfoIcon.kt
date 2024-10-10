package com.perfomer.checkielite.common.ui.cui.widget.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
        InfoIcon(
            isActive = tooltipState.isVisible,
            onClick = { scope.launch { tooltipState.show() } },
        )
    }
}

@Composable
internal fun InfoIcon(
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalCuiPalette.current

    val backgroundColor = if (isActive) palette.BackgroundAccentSecondary else palette.BackgroundSecondary
    val outlineColor = if (isActive) palette.OutlineAccentSecondary else palette.OutlineSecondary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(border = BorderStroke(width = 1.dp, color = outlineColor), shape = CircleShape)
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .size(24.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = palette.TextPrimary,
        )
    }
}