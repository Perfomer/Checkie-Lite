@file:OptIn(ExperimentalMaterial3Api::class)

package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.toolbarDivider
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiOutlineButton
import com.perfomer.checkielite.common.ui.cui.widget.state.AnimatedState
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiGlassScaffold
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.changelog.R
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state.ChangelogUiState
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.widget.ChangelogSkeleton
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
internal fun ChangelogScreen(
    state: ChangelogUiState,
    onBackPress: () -> Unit = {},
    onRetryClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

    CuiGlassScaffold(
        topBar = {
            TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = { Text(stringResource(R.string.changelog_title), fontSize = 18.sp, fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    CuiToolbarNavigationIcon(
                        painter = painterResource(CommonDrawable.ic_cross),
                        color = LocalCuiPalette.current.IconPrimary,
                        onBackPress = onBackPress,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .toolbarDivider(
                        show = shouldShowDivider,
                        strokeColor = LocalCuiPalette.current.OutlineSecondary,
                    )
            )
        },
    ) { contentPadding ->
        AnimatedState(state) { currentState ->
            when (currentState) {
                is ChangelogUiState.Loading -> Loading(contentPadding)
                is ChangelogUiState.Content -> Content(currentState.markdown, contentPadding, scrollState)
                is ChangelogUiState.Error -> Error(contentPadding, onRetryClick)
            }
        }
    }
}

@Composable
private fun Loading(contentPadding: PaddingValues) {
    ChangelogSkeleton(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    )
}

@Composable
private fun Content(
    markdown: String,
    contentPadding: PaddingValues,
    scrollState: androidx.compose.foundation.ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(contentPadding)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        MarkdownText(
            markdown = markdown,
            style = LocalTextStyle.current.copy(
                color = LocalCuiPalette.current.TextPrimary,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            isTextSelectable = true,
            disableLinkMovementMethod = true,
            linkColor = LocalCuiPalette.current.TextAccent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Error(
    contentPadding: PaddingValues,
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CuiBlock(
                title = stringResource(CommonString.common_error_title),
                message = stringResource(CommonString.common_error_message),
                illustrationPainter = painterResource(CommonDrawable.ill_error),
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CuiOutlineButton(
                text = stringResource(R.string.changelog_retry),
                onClick = onRetryClick,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@ScreenPreview
@Composable
private fun ChangelogScreenLoadingPreview() = CheckieLiteTheme {
    ChangelogScreen(state = ChangelogUiState.Loading)
}

@ScreenPreview
@Composable
private fun ChangelogScreenContentPreview() = CheckieLiteTheme {
    ChangelogScreen(state = ChangelogUiState.Content("# Checkie Lite\n\n## Version 1.6.0\n- Example item"))
}
