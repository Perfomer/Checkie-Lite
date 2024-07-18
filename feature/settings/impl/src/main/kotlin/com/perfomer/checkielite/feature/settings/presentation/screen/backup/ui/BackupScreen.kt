package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BackupScreen(state: BackupUiState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backup") },
            )
        },
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            // TODO
        }
    }
}

@ScreenPreview
@Composable
private fun BackupScreenPreview() = CheckieLiteTheme {
    BackupScreen(state = mockUiState)
}

internal val mockUiState = BackupUiState(
    title = "Creating backup...",
    progressPercentDone = 65,
)