package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

const val SharedNavigationTransitionDurationMillis: Int = 250

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalNavigationAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
