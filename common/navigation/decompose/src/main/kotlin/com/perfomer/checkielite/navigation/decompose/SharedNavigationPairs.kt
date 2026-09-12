package com.perfomer.checkielite.navigation.decompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationPair

/** Metadata selected with the animator, before either endpoint is composed. */
internal class SharedNavigationPairs {
    private val entries = mutableMapOf<String, EntryPair>()
    private var preparationVersion by mutableIntStateOf(0)

    /** Recompose the resting endpoint before its visibility starts changing. */
    fun prepare(source: String, target: String, groups: Set<SharedContentGroup>) {
        select(source, target, groups)
        preparationVersion++
    }

    fun select(source: String, target: String, groups: Set<SharedContentGroup>): SharedNavigationPair {
        val existing = listOfNotNull(entries[source], entries[target])
            .firstOrNull { it.source == source && it.target == target }
        val pair = existing
            ?: EntryPair(source, target, SharedNavigationPair(groups))
        entries[source] = pair
        entries[target] = pair
        return pair.scope
    }

    fun forEntry(key: String): SharedNavigationPair? {
        preparationVersion
        return entries[key]?.scope
    }

    fun forget(key: String) {
        entries.remove(key)
    }

    private class EntryPair(val source: String, val target: String, val scope: SharedNavigationPair)
}
