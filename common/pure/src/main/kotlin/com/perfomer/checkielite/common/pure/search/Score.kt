package com.perfomer.checkielite.common.pure.search

import kotlin.math.min
import kotlin.math.roundToInt

private val symbols = listOf(',', '.', ':', '-', ';', '"', '\'', '/', '!', '?', '(', ')', ' ')

private val PunctuationRegex = Regex(
    symbols.joinToString(
        separator = "",
        prefix = "[",
        postfix = "]"
    ) { c -> """\$c""" }
)

fun score(target: String, source: String, weights: DamerauLevenshteinWeights): Int {
    val step = min(target.length, source.length)
    val firstToken: CharSequence = source.subSequence(0, step).stripPunctuation()

    var mostRelevantDistance = damerauLevenshtein(source = firstToken, target = target, weights = weights)
    var mostRelevantStartIndex = 0

    for (endIndex in step + 1..source.length) {
        val startIndex = endIndex - step
        val token = source.subSequence(startIndex, endIndex).stripPunctuation()
        val distance = damerauLevenshtein(source = token, target = target, weights = weights)

        if (distance < mostRelevantDistance) {
            mostRelevantDistance = distance
            mostRelevantStartIndex = startIndex
        }
    }

    return mostRelevantDistance * 10 + ((mostRelevantStartIndex.toFloat() / source.length) * 100).roundToInt()
}

private fun CharSequence.stripPunctuation(): String {
    return replace(PunctuationRegex, "")
}