package com.perfomer.checkielite.common.ui.cui.widget.dropdown

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CuiDropdownIcon(
    painter: Painter = painterResource(R.drawable.ic_menu),
    content: @Composable ColumnScope.() -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    CuiIconButton(
        painter = painter,
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier.offset(x = (-4).dp)
    )

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        content = content,
        offset = DpOffset(x = (-12).dp, y = 0.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .motionEventSpy { event ->
                if (event.action != MotionEvent.ACTION_UP) return@motionEventSpy
                isExpanded = false
            }
    )
}

@Composable
fun CuiDropdownMenuItem(
    text: String,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    iconContentDescription: String? = null,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text) },
        leadingIcon = iconPainter?.let {
            {
                Icon(
                    painter = iconPainter,
                    contentDescription = iconContentDescription,
                )
            }
        },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = modifier,
    )
}