package com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state.ThemeOption
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state.ThemeUiState

@Composable
internal fun ThemeScreen(
    state: ThemeUiState,
    onOptionClick: (ThemeMode) -> Unit = {},
) {
    Column(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Header()

        Spacer(Modifier.height(16.dp))

        state.items.forEach { item ->
            key(item.type) {
                ThemeOptionItem(
                    option = item,
                    onClick = onOptionClick,
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_theme_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@Composable
private fun ThemeOptionItem(
    option: ThemeOption,
    onClick: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(option.type) }
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(option.icon),
            contentDescription = option.type.name,
            tint = LocalCuiPalette.current.IconAccent,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = text(option.text),
            fontSize = 16.sp,
            fontWeight = if (option.isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1F)
        )

        AnimatedVisibility(visible = option.isSelected) {
            Icon(
                painter = painterResource(CommonDrawable.ic_tick),
                contentDescription = null,
                tint = LocalCuiPalette.current.IconPositive,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    ThemeScreen(state = mockUiState)
}

internal val mockUiState = ThemeUiState(
    items = listOf(
        ThemeOption(
            type = ThemeMode.SYSTEM,
            icon = R.drawable.ic_theme_system,
            text = Text.raw("System"),
            isSelected = true,
        ),
        ThemeOption(
            type = ThemeMode.LIGHT,
            icon = R.drawable.ic_theme_light,
            text = Text.raw("Light"),
            isSelected = false,
        ),
        ThemeOption(
            type = ThemeMode.DARK,
            icon = R.drawable.ic_theme_dark,
            text = Text.raw("Dark"),
            isSelected = false,
        ),
    ),
)