package com.perfomer.checkielite.common.ui.cui.widget.block

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
fun CuiBlock(
    title: String,
    message: String,
    illustrationPainter: Painter,
    modifier: Modifier = Modifier,
    illustrationContentDescription: String? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = illustrationPainter,
            contentDescription = illustrationContentDescription,
            modifier = Modifier.width(184.dp),
        )

        CuiSpacer(32.dp)

        Text(
            text = title,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )

        CuiSpacer(8.dp)

        Text(
            text = message,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            color = LocalCuiPalette.current.TextSecondary,
        )
    }
}