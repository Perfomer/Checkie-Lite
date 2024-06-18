package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.icu.text.NumberFormat
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

internal class DecimalInputVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.isEmpty()) return text.toTransformedText(OffsetMapping.Identity)

        var inputText = text.text

        val isEndsWithDecimalSeparator = inputText.lastOrNull() == '.'
        val containsDecimalSeparator = inputText.contains('.')
        val isEndsWithZero = inputText.lastOrNull() == '0'
        val isEndsWithInsignificantZero = containsDecimalSeparator && isEndsWithZero

        // Workaround to draw decimal separator according to the current locale
        var shouldRemoveLastDigitAfterFormatting = false

        if (isEndsWithDecimalSeparator || isEndsWithInsignificantZero) {
            shouldRemoveLastDigitAfterFormatting = true
            // Add any number but 0, because formatter consumes insignificant zeros after decimal separator
            inputText += '1'
        }

        val bigDecimalValue = inputText.toDoubleOrNull() ?: return text.toTransformedText(OffsetMapping.Identity)
        var formattedNumber = formatter.format(bigDecimalValue)

        if (shouldRemoveLastDigitAfterFormatting) {
            formattedNumber = formattedNumber.dropLast(1)
        }

        val formattedText = buildAnnotatedString {
            val separatorIndex = formattedNumber.indexOf(separators.decimalSeparator)
            if (separatorIndex == -1) return@buildAnnotatedString append(formattedNumber)

            append(formattedNumber.subSequence(0, separatorIndex + 1))

            withStyle(SpanStyle(fontSize = 14.sp)) {
                append(formattedNumber.substring(separatorIndex + 1, formattedNumber.length))
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val transformedOffsets = formattedNumber
                    .mapIndexedNotNull { index, char ->
                        index.takeIf { !char.isNumberFormatSeparator }
                            // convert index to an offset
                            ?.plus(1)
                    }
                    // We want to support an offset of 0 and shift everything to the right,
                    // so we prepend that index by default
                    .let { offsetList -> listOf(0) + offsetList }

                return transformedOffsets[offset]
            }

            override fun transformedToOriginal(offset: Int): Int {
                return formattedNumber
                    // This creates a list of all separator offsets
                    .mapIndexedNotNull { index, char ->
                        index.takeIf { char.isNumberFormatSeparator }
                    }
                    // We want to count how many separators precede the transformed offset
                    .count { separatorIndex -> separatorIndex < offset }
                    // We find the original offset by subtracting the number of separators
                    .let { separatorCount -> offset - separatorCount }
            }
        }

        return formattedText.toTransformedText(offsetMapping)
    }

    private companion object {

        private val separators: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
        private val formatter: NumberFormat = DecimalFormat.getInstance()

        private val Char.isNumberFormatSeparator: Boolean
            get() = !isDigit() && this != separators.decimalSeparator

        private fun AnnotatedString.toTransformedText(offsetMapping: OffsetMapping): TransformedText {
            return TransformedText(
                offsetMapping = offsetMapping,
                text = AnnotatedString(
                    text = text,
                    spanStyles = spanStyles,
                    paragraphStyles = paragraphStyles
                ),
            )
        }
    }
}

