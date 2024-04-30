package com.perfomer.checkielite.core.entity.search

import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.entity.sort.SortingOrder
import java.io.Serializable

data class SearchSorting(
    val order: SortingOrder = SortingOrder.DESCENDING,
    val strategy: ReviewsSortingStrategy = ReviewsSortingStrategy.CREATION_DATE,
) : Serializable