package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.progress.CuiProgressBar
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.span.annotatedStringResource
import com.perfomer.checkielite.common.ui.util.span.appNameSpannable
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiState

@Composable
internal fun BackupScreen(state: BackupUiState) {
    Scaffold(
        topBar = { BackupTopAppBar() },
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(vertical= 32.dp, horizontal = 24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ill_backup),
                contentDescription = null,
                modifier = Modifier.width(170.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = state.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            )

            Spacer(Modifier.height(24.dp))

            if (state.backupProgress != null && state.progressLabel != null) {
                CuiProgressBar(
                    progress = state.backupProgress,
                    modifier = Modifier.width(160.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = state.progressLabel,
                    fontSize = 14.sp,
                    color = LocalCuiPalette.current.TextSecondary
                )
            }

            Spacer(Modifier.weight(1F))

            Text(
                text = annotatedStringResource(R.string.settings_backup_minimize_hint),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BackupTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = appNameSpannable(), fontSize = 20.sp) },
        modifier = Modifier.fillMaxWidth()
    )
}

@ScreenPreview
@Composable
private fun BackupScreenPreview() = CheckieLiteTheme {
    BackupScreen(state = mockUiState)
}

internal val mockUiState = BackupUiState(
    title = "Creating backup...",
    backupProgress = 0.65F,
    progressLabel = "65%",
)