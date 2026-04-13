package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.animation.animateBounds
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsLibrarySection(
    tags: List<TagsPageUiState.Tag>,
    searchQuery: String,
    shouldShowAddTag: Boolean,
    palette: CuiPalette,
    sectionBorderColor: Color,
    onSearchQueryInput: (String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
    onCreateTagClick: () -> Unit,
    onTagClick: (String) -> Unit,
    onTagLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        TagsSearchField(
            searchQuery = searchQuery,
            onSearchQueryInput = onSearchQueryInput,
            onSearchQueryClearClick = onSearchQueryClearClick,
        )

        CuiSpacer(6.dp)

        LookaheadScope {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (shouldShowAddTag) {
                    TagsAddTagChip(
                        searchQuery = searchQuery.takeIf { it.isNotBlank() },
                        onClick = onCreateTagClick,
                    )
                }

                for (tag in tags) {
                    key(tag.id) {
                        TagsTagChip(
                            tag = tag,
                            palette = palette,
                            sectionBorderColor = sectionBorderColor,
                            isRecommended = tag.isRecommended,
                            onClick = onTagClick,
                            onLongClick = onTagLongClick,
                            modifier = Modifier.animateBounds(this@LookaheadScope)
                        )
                    }
                }
            }
        }
    }
}
