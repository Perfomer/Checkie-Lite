package com.perfomer.checkielite.common.ui.cui.widget.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.gigamole.composefadingedges.FadingEdgesGravity
import com.gigamole.composefadingedges.horizontalFadingEdges
import com.perfomer.checkielite.common.pure.util.splitAtIndex
import com.perfomer.checkielite.common.ui.cui.modifier.conditional
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiFadedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current,
    fadeWidth: Dp = 40.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val textColor = color.takeOrElse { style.color.takeOrElse { LocalContentColor.current } }
    var width by remember { mutableIntStateOf(0) }
    val localStyle = TextStyle(
        color = textColor,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign ?: TextAlign.Unspecified,
        lineHeight = lineHeight,
        fontFamily = fontFamily,
        textDecoration = textDecoration,
        fontStyle = fontStyle,
        letterSpacing = letterSpacing
    )

    val textLayoutResult = remember(width) {
        textMeasurer.measure(
            text = text,
            style = style.merge(localStyle),
            constraints = Constraints.fixedWidth(width),
        )
    }
    val shouldFade = remember(width) {
        textLayoutResult.lineCount > maxLines && width > 0
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .onSizeChanged { width = it.width }
    ) {
        if (shouldFade && maxLines > 1) {
            val lastLineStartIndex = textLayoutResult.getLineStart(maxLines - 1)
            val (firstLines, lastLines) = text.splitAtIndex(lastLineStartIndex)

            Column(modifier = Modifier.fillMaxWidth()) {
                BasicText(
                    text = firstLines,
                    style = style.merge(localStyle),
                    minLines = minLines,
                    modifier = modifier
                )

                BasicText(
                    text = lastLines,
                    style = style.merge(localStyle),
                    softWrap = false,
                    minLines = minLines,
                    modifier = Modifier.horizontalFadingEdges(
                        gravity = FadingEdgesGravity.End,
                        length = fadeWidth,
                    )
                )
            }
        } else {
            BasicText(
                text = text,
                style = style.merge(localStyle),
                maxLines = maxLines,
                softWrap = false,
                minLines = minLines,
                modifier = Modifier.conditional(shouldFade) {
                    horizontalFadingEdges(
                        gravity = FadingEdgesGravity.End,
                        length = fadeWidth,
                    )
                }
            )
        }
    }
}

@Composable
@WidgetPreview
private fun TextWithFadedEndPreview() = CheckieLiteTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        ) {
            CuiFadedText(
                text = "Люди говорят, но слова их пусты, как скорлупа. Они верят в смысл, которого нет. Цепляются за дела, за любовь, за завтра — но всё это лишь жесты в пустоте. Я сижу среди этого. Не живу — просто нахожусь, как пыль на подоконнике. Мир не враждебен. Он просто есть. И в этом — вся его жестокая насмешка.",
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        ) {
            CuiFadedText(
                text = "Люди говорят, но слова их пусты, как скорлупа. Они верят в смысл, которого нет. Цепляются за дела, за любовь, за завтра — но всё это лишь жесты в пустоте. Я сижу среди этого. Не живу — просто нахожусь, как пыль на подоконнике. Мир не враждебен. Он просто есть. И в этом — вся его жестокая насмешка.",
                maxLines = 6,
                fadeWidth = 200.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(200.dp)
                .background(Red)
                .padding(bottom = 50.dp)
        ) {
            CuiFadedText(
                text = "Люди говорят, но слова их пусты, как скорлупа. Они верят в смысл, которого нет. Цепляются за дела, за любовь, за завтра — но всё это лишь жесты в пустоте. Я сижу среди этого. Не живу — просто нахожусь, как пыль на подоконнике. Мир не враждебен. Он просто есть. И в этом — вся его жестокая насмешка.",
                color = Color.Blue,
                modifier = Modifier.width(200.dp)
            )
        }
    }
}