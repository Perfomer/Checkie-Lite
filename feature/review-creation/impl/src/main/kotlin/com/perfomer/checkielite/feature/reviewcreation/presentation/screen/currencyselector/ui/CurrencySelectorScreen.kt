package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.conditional
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField2
import com.perfomer.checkielite.common.ui.cui.widget.scrim.verticalScrimBrush
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.add
import com.perfomer.checkielite.common.ui.util.keyboardOpenedAsState
import com.perfomer.checkielite.common.ui.util.pxToDp
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencyItem
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencySelectorUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.widget.CurrencySymbol
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
internal fun CurrencySelectorScreen(
    state: CurrencySelectorUiState,

    onSearchQueryInput: (value: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
    onCurrencyClick: (code: String) -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    BoxWithConstraints {
        val maxHeight = remember(constraints.maxHeight) {
            constraints.maxHeight.pxToDp().dp * 0.8F
        }

        var shouldAnimateContentSize by remember { mutableStateOf(false) }

        LaunchedEffect(shouldAnimateContentSize) {
            delay(1_000L)
            shouldAnimateContentSize = true
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .requiredHeightIn(max = maxHeight)
                .conditional(shouldAnimateContentSize) { animateContentSize() }
        ) {
            Column {
                val scrollState = rememberLazyListState()
                val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

                val isKeyboardOpened by keyboardOpenedAsState()
                val emptyInsets = remember { PaddingValues() }
                val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()
                val actualNavigationBarsPadding = if (isKeyboardOpened) emptyInsets else navigationBarsPadding

                Header(showDivider = shouldShowDivider)

                LaunchedEffect(state.searchQuery) {
                    snapshotFlow { scrollState.firstVisibleItemIndex }
                        .collect { scrollState.scrollToItem(0) }
                }

                LazyColumn(
                    state = scrollState,
                    contentPadding = actualNavigationBarsPadding.add(top = 72.dp, bottom = 100.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.currencies.isEmpty()) {
                        item(
                            contentType = "empty",
                            key = "empty",
                        ) {
                            Text(
                                text = stringResource(CommonString.common_nothing_found),
                                color = LocalCuiPalette.current.TextSecondary,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp)
                            )
                        }
                    }

                    items(
                        items = state.currencies,
                        contentType = { "currency" },
                        key = { it.code }
                    ) { currency ->
                        Currency(
                            currency = currency,
                            onClick = { onCurrencyClick(currency.code) },
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp)
        ) {
            SearchField(
                searchQuery = state.searchQuery,
                onSearchQueryInput = onSearchQueryInput,
                onSearchQueryClearClick = {
                    shouldAnimateContentSize = false
                    focusManager.clearFocus()
                    onSearchQueryClearClick()
                },
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
                .background(verticalScrimBrush())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            CuiPrimaryButton(
                text = stringResource(CommonString.common_done),
                onClick = onDoneClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Currency(
    currency: CurrencyItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        CurrencySymbol(
            currencySymbol = currency.symbol,
            isSelected = currency.isSelected,
        )

        Text(
            text = currency.displayName,
            fontSize = 16.sp,
            fontWeight = if (currency.isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1F)
        )

        AnimatedVisibility(visible = currency.isSelected) {
            Icon(
                painter = painterResource(CommonDrawable.ic_tick),
                contentDescription = null,
                tint = LocalCuiPalette.current.IconPositive,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun Header(
    showDivider: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_currencyselector_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
    }

    AnimatedVisibility(visible = showDivider, enter = fadeIn(tween(250)), exit = fadeOut(tween(250))) {
        HorizontalDivider(
            color = LocalCuiPalette.current.OutlineSecondary,
        )
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CuiOutlinedField2(
        value = searchQuery,
        onValueChange = onSearchQueryInput,
        placeholder = stringResource(CommonString.common_search),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(CommonString.common_clear),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchQueryClearClick,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            }
        },
        modifier = modifier.background(
            color = LocalCuiPalette.current.BackgroundPrimary,
            shape = RoundedCornerShape(16.dp),
        )
    )
}

@ScreenPreview
@Composable
private fun CurrencySelectorScreenPreview() = CheckieLiteTheme {
    CurrencySelectorScreen(state = mockUiState)
}

internal val mockUiState = CurrencySelectorUiState(
    searchQuery = "",
    currencies = persistentListOf(
        CurrencyItem("1", "US Dollar", "$", false),
        CurrencyItem("2", "US Dollar", "KMF", true),
        CurrencyItem("3", "US Dollar", "$", false),
        CurrencyItem("4", "US Dollar", "$", false),
        CurrencyItem("5", "US Dollar", "KMF", false),
    ),
)