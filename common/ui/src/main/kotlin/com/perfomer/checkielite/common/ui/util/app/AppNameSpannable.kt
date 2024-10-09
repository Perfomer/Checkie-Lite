package com.perfomer.checkielite.common.ui.util.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.util.isDebug

@Composable
@ReadOnlyComposable
fun appNameSpannable(): AnnotatedString {
    val textResource = if (isDebug()) R.string.app_name_lite_debug else R.string.app_name_lite

    return buildAnnotatedString {
        append(
            AnnotatedString(
                text = stringResource(R.string.app_name_checkie),
                spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
            )
        )

        append(" ")

        append(
            AnnotatedString(
                text = stringResource(textResource),
                spanStyle = SpanStyle(fontWeight = FontWeight.Normal)
            )
        )
    }
}