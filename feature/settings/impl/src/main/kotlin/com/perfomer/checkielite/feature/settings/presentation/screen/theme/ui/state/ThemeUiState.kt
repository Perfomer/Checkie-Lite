package com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

@Immutable
internal data class ThemeUiState(
    val items: List<ThemeOption>,
)

internal data class ThemeOption(
    val type: ThemeMode,
    @DrawableRes val icon: Int,
    val text: Text,
    val isSelected: Boolean,
)