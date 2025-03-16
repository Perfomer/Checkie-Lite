package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.TagsStore
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEffect.CloseKeyboard
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnSearchQueryClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnSearchQueryInput
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnTagClick

internal class TagsContentScreen(
    private val store: TagsStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        val focusManager = LocalFocusManager.current

        EffectHandler { effect ->
            when (effect) {
                CloseKeyboard -> focusManager.clearFocus()
            }
        }

        TagsScreen(
            state = state,
            onTagClick = acceptable(::OnTagClick),
            onSearchQueryInput = acceptable(::OnSearchQueryInput),
            onSearchQueryClearClick = acceptable(OnSearchQueryClearClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}