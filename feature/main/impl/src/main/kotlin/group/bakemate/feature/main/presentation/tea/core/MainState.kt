package group.bakemate.feature.main.presentation.tea.core

import group.bakemate.feature.main.presentation.entity.ContentType
import group.bakemate.feature.main.presentation.entity.ContentType.ORDERS

internal data class MainState(
    val currentContentType: ContentType = ORDERS
)