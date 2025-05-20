package com.perfomer.checkielite.feature.settings.presentation.screen.libraries.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastDistinctBy
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.util.withContext
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun LibrariesScreen() {
    val lazyListState = rememberLazyListState()

    LibrariesContainer(
        librariesBlock = { context ->
            val libs = Libs.Builder().withContext(context).build()
            libs.copy(
                libraries = libs.libraries.fastDistinctBy { it.name + it.artifactVersion }
                    .toImmutableList(),
            )
        },
        showDescription = true,
        lazyListState = lazyListState,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
@ScreenPreview
private fun LibrariesScreenPreview() = CheckieLiteTheme {
    LibrariesScreen()
}