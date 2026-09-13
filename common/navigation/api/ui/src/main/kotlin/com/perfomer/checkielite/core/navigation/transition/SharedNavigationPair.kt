package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.runtime.compositionLocalOf

val LocalSharedNavigationEndpoint = compositionLocalOf<SharedNavigationEndpoint?> { null }

/** Side refers to the registered edge, and stays unchanged when navigating back. */
data class SharedNavigationEndpoint(val pair: SharedNavigationPair, val isSource: Boolean)

/** One pair of navigation entries, retained through seeking, rollback and completion. */
class SharedNavigationPair(private val policy: SharedTransitionPolicy) {
    constructor(groups: Set<SharedContentGroup>) : this(SharedTransitionPolicy.forGroups(groups))

    fun allows(group: SharedContentGroup): Boolean = group in policy.groups

    fun match(role: SharedContentRole, isSource: Boolean): SharedContentMatch? = policy.match(role, isSource)
}

/** Separates groups and navigation pairs even when their local content IDs are equal. */
data class SharedNavigationContentKey(
    val pair: SharedNavigationPair?,
    val group: SharedContentGroup,
    val id: Any,
    val match: SharedContentMatch?,
)
