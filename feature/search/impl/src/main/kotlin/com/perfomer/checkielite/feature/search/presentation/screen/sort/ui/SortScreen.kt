package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.feature.search.R
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortingOption

@Composable
internal fun SortScreen(
    state: SortUiState,
    onOptionClick: (ReviewsSortingStrategy) -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Header()

        Spacer(Modifier.height(16.dp))

        state.items.forEach { item ->
            key(item.type) {
                SortingOptionItem(
                    option = item,
                    onClick = onOptionClick,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        CuiPrimaryButton(
            text = stringResource(CommonString.common_done),
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

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
            text = stringResource(R.string.search_sort_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@Composable
private fun SortingOptionItem(
    option: SortingOption,
    onClick: (ReviewsSortingStrategy) -> Unit,
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
        Text(
            text = option.text,
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
    SortScreen(state = mockUiState)
}

internal val mockUiState = SortUiState(
    items = listOf(
        SortingOption(
            type = ReviewsSortingStrategy.NEWEST,
            text = "Creation date",
            isSelected = true,
        ),
        SortingOption(
            type = ReviewsSortingStrategy.MOST_RATED,
            text = "Rating",
            isSelected = false,
        ),
    ),
)