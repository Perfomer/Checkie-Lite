package com.perfomer.checkielite.common.pure.search

import kotlin.math.roundToInt

private val weights: DamerauLevenshteinWeights = DamerauLevenshteinWeights(
    deleteCost = 15,
    insertCost = 15,
    replaceCost = 10,
    swapCost = 10,
)

fun <T> List<T>.smartFilterByQuery(
    query: String,
    fieldProvider: T.() -> List<Pair<String?, Float>>,
): List<T> {
    fun calculateScore(value: String?, factor: Float): Int {
        return if (value.isNullOrBlank()) Int.MAX_VALUE
        else (score(target = query, source = value, weights = weights) * factor).roundToInt()
    }

    if (query.isEmpty()) return this

    val maxScore = ((query.length / 2.5F).toInt() * 200).coerceIn(100, 300)

    return map { item ->
        val score = fieldProvider.invoke(item)
            .minOf { (value, factor) -> calculateScore(value, factor) }

        item to score
    }
        .filter { (_, score) -> score < maxScore }
        .sortedBy { (_, score) -> score }
        .map { (review, _) -> review }
}