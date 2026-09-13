package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedFieldDefaults
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewcreation.R

@Composable
internal fun TagsSearchField(
    searchQuery: String,
    onSearchQueryInput: (String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    CuiOutlinedField(
        text = searchQuery,
        placeholder = stringResource(R.string.reviewcreation_tags_field_search),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        singleLine = true,
        onValueChange = onSearchQueryInput,
        colors = CuiOutlinedFieldDefaults.surfaceColors(),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            } else {
                CuiIconButton(
                    painter = painterResource(CommonDrawable.ic_cross),
                    contentDescription = stringResource(CommonString.common_clear),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchQueryClearClick,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
