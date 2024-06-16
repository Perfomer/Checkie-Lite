package com.perfomer.checkielite.common.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.copy(
    top: Dp = calculateTopPadding(),
    bottom: Dp = calculateBottomPadding(),
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
): PaddingValues {
    return PaddingValues(
        top = top,
        bottom = bottom,
        start = start,
        end = end,
    )
}

@Composable
operator fun PaddingValues.plus(values: PaddingValues):PaddingValues {
    return PaddingValues(
        top = calculateTopPadding() + values.calculateTopPadding(),
        bottom = calculateBottomPadding() + values.calculateBottomPadding(),
        start = calculateStartPadding(LocalLayoutDirection.current) + values.calculateStartPadding(LocalLayoutDirection.current),
        end = calculateEndPadding(LocalLayoutDirection.current) + values.calculateEndPadding(LocalLayoutDirection.current),
    )
}