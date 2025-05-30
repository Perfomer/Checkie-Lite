package com.perfomer.checkielite.common.ui.cui.widget.chip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer

@Composable
fun CuiTagChip(
    text: String,
    emoji: String?,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    val selectedChipStyle = CuiChipStyle.selected()
    val defaultChipStyle = CuiChipStyle.default()
    val style = if (isSelected) selectedChipStyle else defaultChipStyle

    CuiChip(
        onClick = onClick,
        onLongClick = onLongClick,
        style = style,
        content = {
            Row {
                if (emoji != null) {
                    Text(text = emoji)

                    CuiSpacer(4.dp)
                }

                Box {
                    // This text is always invisible.
                    // It needed to prevent chip width jumping,
                    // when selected text receives Bold font weight.
                    Text(
                        text = text,
                        fontWeight = selectedChipStyle.fontWeight,
                        modifier = Modifier.alpha(0F)
                    )

                    Text(text)
                }
            }
        },
    )
}