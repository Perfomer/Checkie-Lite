package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.SharedTransitionDestination
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

internal class SearchDestinationTest {

    @Test
    fun `search participates in review shared transitions`() {
        val destination = SearchDestination(tagId = null)

        assertInstanceOf(SharedTransitionDestination::class.java, destination)
        assertEquals("search", destination.sharedTransitionGroup)
    }
}
