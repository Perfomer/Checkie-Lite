package com.perfomer.checkielite.common.pure.util

import kotlin.math.max
import kotlin.math.min

/**
 * Compute the Damerau-Levenshtein distance between the specified source
 * string and the specified target string.
 */
fun damerauLevenshtein(
    source: String,
    target: String,
    ignoreCase: Boolean = true,
    weights: DamerauLevenshteinWeights = DamerauLevenshteinWeights.default,
): Int {
    if (source.isEmpty()) {
        return target.length * weights.insertCost
    }

    if (target.isEmpty()) {
        return source.length * weights.deleteCost
    }

    val table = Array(source.length) { IntArray(target.length) }

    val sourceIndexByCharacter = HashMap<Char, Int>()
    if (!source[0].equals(target[0], ignoreCase)) {
        table[0][0] = min(weights.replaceCost, weights.deleteCost + weights.insertCost)
    }
    sourceIndexByCharacter[source[0]] = 0

    for (i in 1 until source.length) {
        val deleteDistance = table[i - 1][0] + weights.deleteCost
        val insertDistance = (i + 1) * weights.deleteCost + weights.insertCost
        val matchDistance = i * weights.deleteCost + if (source[i].equals(target[0], ignoreCase)) 0 else weights.replaceCost
        table[i][0] = minOf(deleteDistance, insertDistance, matchDistance)
    }

    for (j in 1 until target.length) {
        val deleteDistance = table[0][j - 1] + weights.insertCost
        val insertDistance = (j + 1) * weights.insertCost + weights.deleteCost
        val matchDistance = j * weights.insertCost + if (source[0].equals(target[j], ignoreCase)) 0 else weights.replaceCost
        table[0][j] = minOf(deleteDistance, insertDistance, matchDistance)
    }

    for (i in 1 until source.length) {
        var maxSourceLetterMatchIndex = if (source[i].equals(target[0], ignoreCase)) 0 else -1

        for (j in 1 until target.length) {
            val candidateSwapIndex: Int? = sourceIndexByCharacter[target[j]]
            val jSwap = maxSourceLetterMatchIndex
            val deleteDistance = table[i - 1][j] + weights.deleteCost
            val insertDistance = table[i][j - 1] + weights.insertCost
            var matchDistance = table[i - 1][j - 1]
            if (source[i].equals(target[j], ignoreCase)) {
                maxSourceLetterMatchIndex = j
            } else {
                matchDistance += weights.replaceCost
            }
            var swapDistance = Integer.MAX_VALUE;
            if (candidateSwapIndex != null && jSwap != -1) {
                swapDistance = 0
                if (candidateSwapIndex > 0 || jSwap > 0) {
                    swapDistance = table[max(0, candidateSwapIndex - 1)][max(0, jSwap - 1)]
                }
                swapDistance += (i - candidateSwapIndex - 1) * weights.deleteCost
                swapDistance += (j - jSwap - 1) * weights.insertCost + weights.swapCost
            }
            table[i][j] = intArrayOf(deleteDistance, insertDistance, matchDistance, swapDistance).min()
        }

        sourceIndexByCharacter[source[i]] = i
    }
    return table[source.length - 1][target.length - 1]
}

class DamerauLevenshteinWeights(
    val deleteCost: Int,
    val insertCost: Int,
    val replaceCost: Int,
    val swapCost: Int
) {
    companion object {
        val default = DamerauLevenshteinWeights(1, 1, 1, 1)
    }
}