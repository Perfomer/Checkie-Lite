package com.perfomer.checkielite.navigation.decompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationEndpoint
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationPair
import com.perfomer.checkielite.core.navigation.transition.SharedTransitionPolicy

/** Metadata selected with the animator, before either endpoint is composed. */
internal class SharedNavigationPairs {
    private val entries = mutableMapOf<String, EntryPair>()
    private var preparationVersion by mutableIntStateOf(0)

    /** Recompose the resting endpoint before its visibility starts changing. */
    fun prepare(source: String, target: String, policy: SharedTransitionPolicy) {
        select(source, target, policy)
        preparationVersion++
    }

    fun select(source: String, target: String, groups: Set<SharedContentGroup>): SharedNavigationPair {
        return select(source, target, SharedTransitionPolicy.forGroups(groups))
    }

    fun select(source: String, target: String, policy: SharedTransitionPolicy): SharedNavigationPair {
        val existing = listOfNotNull(entries[source], entries[target])
            .firstOrNull { it.source == source && it.target == target }
        val pair = existing
            ?: EntryPair(source, target, SharedNavigationPair(policy))
        entries[source] = pair
        entries[target] = pair
        return pair.scope
    }

    fun forEntry(key: String): SharedNavigationPair? {
        preparationVersion
        return entries[key]?.scope
    }

    fun endpointForEntry(key: String): SharedNavigationEndpoint? {
        val pair = forEntry(key) ?: return null
        return SharedNavigationEndpoint(pair, isSource = entries.getValue(key).source == key)
    }

    fun forget(key: String) {
        entries.remove(key)
    }

    private class EntryPair(val source: String, val target: String, val scope: SharedNavigationPair)
}
