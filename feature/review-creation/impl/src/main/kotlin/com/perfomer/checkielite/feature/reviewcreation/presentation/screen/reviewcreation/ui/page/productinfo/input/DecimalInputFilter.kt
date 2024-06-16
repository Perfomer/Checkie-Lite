package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input

internal class DecimalInputFilter {

    fun cleanUp(input: String): String {
        if (input.matches(emptyRegex)) return ""
        if (input.matches(onlyZerosRegex)) return "0"

        val sb = StringBuilder()

        var hasDecimalSeparator = false

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }

            if (char in decimalSeparator && !hasDecimalSeparator && sb.isNotEmpty()) {
                hasDecimalSeparator = true
                sb.append('.')
            }
        }

        return sb.toString()
    }

    private companion object {

        private val decimalSeparator: Set<Char> = setOf(',', '.')

        private val emptyRegex: Regex = "\\D".toRegex()
        private val onlyZerosRegex: Regex = "0+".toRegex()
    }
}