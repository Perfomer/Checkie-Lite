package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.presentation.transition.sharedSearchField
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationContent
import com.perfomer.checkielite.feature.search.presentation.transition.SearchFieldContent

@Composable
internal fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) = SharedNavigationContent(group = SearchFieldContent, id = Unit) {
    val palette = LocalCuiPalette.current
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = searchQuery,
        onValueChange = onSearchQueryInput,
        singleLine = true,
        interactionSource = interactionSource,
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = palette.TextPrimary),
        cursorBrush = SolidColor(palette.TextAccent),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        decorationBox = { innerTextField ->
            Surface(shape = CircleShape, color = palette.BackgroundElevationBase) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .padding(start = 16.dp, end = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(CommonDrawable.ic_search),
                        tint = palette.IconSecondary,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Box(modifier = Modifier.weight(1F).padding(vertical = 10.dp)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = stringResource(CommonString.common_search),
                                fontSize = 16.sp,
                                color = palette.TextSecondary,
                            )
                        }
                        innerTextField()
                    }

                    if (searchQuery.isNotEmpty()) {
                        CuiIconButton(
                            painter = painterResource(CommonDrawable.ic_cross),
                            contentDescription = stringResource(CommonString.common_clear),
                            tint = palette.IconSecondary,
                            onClick = onSearchQueryClearClick,
                        )
                    }
                }
            }
        },
        modifier = modifier
            .sharedSearchField()
            .softShadow(interactionSource = interactionSource, shape = CircleShape)
    )
}
