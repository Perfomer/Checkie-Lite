package com.perfomer.checkielite.common.ui.util

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith

fun crossfade(
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
): ContentTransform {
    return fadeIn(animationSpec).togetherWith(fadeOut(animationSpec))
}

fun scale(
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
): ContentTransform {
    return scaleIn(animationSpec).togetherWith(scaleOut(animationSpec))
}

operator fun ContentTransform.plus(other: ContentTransform): ContentTransform {
    return ContentTransform(
        targetContentEnter = this.targetContentEnter + other.targetContentEnter,
        initialContentExit = this.initialContentExit + other.initialContentExit,
    )
}