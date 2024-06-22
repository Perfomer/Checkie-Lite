package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input

import com.perfomer.checkielite.common.pure.util.toRegex

internal class DecimalInputFilter {

    fun cleanUp(input: String): String {
        if (input.isEmpty()) return ""
        if (input.matches(decimalSeparatorsRegex)) return ""

        val parts = input
            .filter { char -> char.isDigit() || char in decimalSeparators }
            .split(decimalSeparatorsRegex)
            .take(2)

        val fractionalPart = parts.getOrNull(1)
            ?.take(MAX_FRACTIONAL_PART_LENGTH)
            .orEmpty()

        val separator = ".".takeIf { parts.size > 1 }.orEmpty()

        val integerPart = parts.first()
            .dropWhile { it == '0' }
            .ifEmpty { "0" }
            .take(MAX_INTEGER_PART_LENGTH)

        return "$integerPart$separator$fractionalPart"
    }

    private companion object {

        private const val MAX_INTEGER_PART_LENGTH = 9
        private const val MAX_FRACTIONAL_PART_LENGTH = 2

        private val decimalSeparators: Set<Char> = setOf(',', '.')
        private val decimalSeparatorsRegex: Regex = decimalSeparators.toRegex()
    }
}