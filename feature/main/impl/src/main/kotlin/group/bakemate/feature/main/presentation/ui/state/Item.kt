package group.bakemate.feature.main.presentation.ui.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import group.bakemate.feature.main.presentation.entity.ContentType

@Stable
internal data class Item(
    @DrawableRes
    val icon: Int,
    @DrawableRes
    val filledIcon: Int,
    @StringRes
    val text: Int,
    val type: ContentType,
    val isSelected: Boolean,
)