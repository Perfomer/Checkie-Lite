package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationElement
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationElement

private data object SearchFieldElement : SharedNavigationElement

/** Connects a search shortcut to its editable field, preserving text size as the bounds change. */
@Composable
fun Modifier.sharedSearchField(): Modifier = sharedNavigationElement(
    element = SearchFieldElement,
    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
)
