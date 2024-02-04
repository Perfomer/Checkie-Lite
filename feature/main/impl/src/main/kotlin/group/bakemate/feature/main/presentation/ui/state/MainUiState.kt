package group.bakemate.feature.main.presentation.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class MainUiState(
    val searchQuery: String,
    val reviews: List<ReviewItem>,
)

@Immutable
internal data class ReviewItem(
    val id: String,
    val title: String,
    val brand: String?,
    val imageUri: String?,
    val rating: Int,
    val emoji: String,
)