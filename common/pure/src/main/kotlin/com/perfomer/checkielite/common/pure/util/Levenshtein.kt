package com.perfomer.checkielite.common.pure.util

import kotlin.math.min

fun levenshtein(first: CharSequence, second: CharSequence): Int {
    if (first == second) return 0
    if (first.isEmpty()) return second.length
    if (second.isEmpty()) return first.length

    val firstLength = first.length + 1
    val secondLength = second.length + 1

    var cost = Array(firstLength) { it }
    var newCost = Array(firstLength) { 0 }

    for (i in 1..<secondLength) {
        newCost[0] = i

        for (j in 1..<firstLength) {
            val match = if (first[j - 1] == second[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[firstLength - 1]
}