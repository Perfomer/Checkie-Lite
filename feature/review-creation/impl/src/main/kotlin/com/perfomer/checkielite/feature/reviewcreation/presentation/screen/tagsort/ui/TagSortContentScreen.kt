package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.TagSortStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnSortingOptionClick

internal class TagSortContentScreen(
    private val store: TagSortStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        BackHandler { accept(OnBackPress) }

        TagSortScreen(
            state = state,
            onOptionClick = acceptable(::OnSortingOptionClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}