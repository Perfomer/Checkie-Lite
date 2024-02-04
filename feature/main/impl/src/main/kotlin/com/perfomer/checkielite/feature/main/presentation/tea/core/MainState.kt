package com.perfomer.checkielite.feature.main.presentation.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal data class MainState(
    val reviews: List<CheckieReview> = emptyList(),
    val searchQuery: String = "",
)