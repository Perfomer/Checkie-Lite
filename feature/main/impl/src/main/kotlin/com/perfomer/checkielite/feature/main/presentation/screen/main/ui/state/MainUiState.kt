package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.main.R
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface MainUiState {

    data object Loading : MainUiState

    data class Content(
        val tags: ImmutableList<Tag>,
        val reviews: ImmutableList<ReviewItem>,
        val whatsNewBanner: WhatsNewBanner?,
    ) : MainUiState

    data object Empty : MainUiState

    data object Error : MainUiState
}

@Immutable
data class Tag(
    val id: String,
    val value: String,
    val emoji: String?,
)

@Immutable
internal data class WhatsNewBanner(
    val title: Text,
) {
    val subtitle: Text = Text.resource(R.string.main_changelog_subtitle)
    val badge: Text = Text.resource(R.string.main_changelog_badge)
}
