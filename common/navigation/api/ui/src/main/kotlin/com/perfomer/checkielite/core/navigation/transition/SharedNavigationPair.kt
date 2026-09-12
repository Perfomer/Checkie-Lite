package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.runtime.compositionLocalOf

val LocalSharedNavigationPair = compositionLocalOf<SharedNavigationPair?> { null }

/** One pair of navigation entries, retained through seeking, rollback and completion. */
class SharedNavigationPair(groups: Set<SharedContentGroup>) {
    private val groups = groups.toSet()

    fun allows(group: SharedContentGroup): Boolean = group in groups
}

/** Separates groups and navigation pairs even when their local content IDs are equal. */
data class SharedNavigationContentKey(
    val pair: SharedNavigationPair?,
    val group: SharedContentGroup,
    val id: Any,
)
