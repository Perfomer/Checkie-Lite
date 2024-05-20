package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsParams
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.TagsStore
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnTagClick
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class TagsContentScreen(
    private val params: TagsParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<TagsStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        TagsScreen(
            state = state,
            onTagClick = acceptable(::OnTagClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}