package com.perfomer.checkielite.common.ui.cui.widget.dropdown

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.collections.immutable.ImmutableList

/**
 * Wrapper providing suggestions dropdown menu
 *
 * To make it work:
 * - The [menuAnchor][ExposedDropdownMenuBoxScope.menuAnchor] modifier should be passed to the text field.
 * - The [contentInteractionSource] parameter should be also passed to the text field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuiSuggestionsBox(
    currentValue: String,
    suggestions: ImmutableList<String>,
    contentInteractionSource: InteractionSource,
    onSuggestionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ExposedDropdownMenuBoxScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isContentFocused by contentInteractionSource.collectIsFocusedAsState()

    var isSuggestionsExpandRequested by remember(currentValue) { mutableStateOf(true) }
    val isSuggestionsCanBeExpanded by remember(suggestions) {
        derivedStateOf { isContentFocused && suggestions.isNotEmpty() }
    }

    val isSuggestionsExpanded = isSuggestionsCanBeExpanded && isSuggestionsExpandRequested

    ExposedDropdownMenuBox(
        expanded = isSuggestionsExpanded,
        onExpandedChange = { shouldExpand -> isSuggestionsExpandRequested = shouldExpand },
        modifier = modifier
    ) {
        content()

        ExposedDropdownMenu(
            expanded = isSuggestionsExpanded,
            onDismissRequest = { isSuggestionsExpandRequested = false },
        ) {
            suggestions.forEach { brand ->
                CuiDropdownMenuItem(
                    text = brand,
                    onClick = {
                        onSuggestionSelected(brand)
                        isSuggestionsExpandRequested = false
                        focusManager.clearFocus()
                    },
                )
            }
        }
    }
}