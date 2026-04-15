package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core

import com.perfomer.checkielite.common.pure.state.Lce

internal data class ChangelogState(
    val changelog: Lce<String> = Lce.initial(),
)