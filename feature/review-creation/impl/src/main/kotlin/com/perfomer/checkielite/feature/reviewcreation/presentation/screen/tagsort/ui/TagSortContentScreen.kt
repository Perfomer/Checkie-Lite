package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.TagSortStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnSortingOptionClick
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class TagSortContentScreen(
    private val params: TagSortParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<TagSortStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        TagSortScreen(
            state = state,
            onOptionClick = acceptable(::OnSortingOptionClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}