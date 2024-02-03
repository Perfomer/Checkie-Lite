package group.bakemate.feature.main.presentation.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class MainUiState(
    val items: List<CheckieItem>,
)

@Immutable
internal data class CheckieItem(
    val id: String,
    val title: String,
    val brand: String?,
    val imageUri: String?,
    val rating: Int,
    val emoji: String,
)