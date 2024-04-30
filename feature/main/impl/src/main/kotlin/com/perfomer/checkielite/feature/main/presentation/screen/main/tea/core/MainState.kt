package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.entity.CheckieReview

internal data class MainState(
    val reviews: Lce<List<CheckieReview>> = Lce.initial(),
)