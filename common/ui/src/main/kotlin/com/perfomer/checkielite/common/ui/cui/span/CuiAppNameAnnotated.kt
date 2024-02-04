package com.perfomer.checkielite.common.ui.cui.span

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.theme.OrangeDark

@Composable
fun cuiAppNameAnnotated(): AnnotatedString {
    return AnnotatedString(text = " ") +
            AnnotatedString(
                text = stringResource(id = R.string.app_name),
                spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
            ) +
            AnnotatedString(
                text = ".",
                spanStyle = SpanStyle(color = OrangeDark, fontWeight = FontWeight.Bold),
            )
}

