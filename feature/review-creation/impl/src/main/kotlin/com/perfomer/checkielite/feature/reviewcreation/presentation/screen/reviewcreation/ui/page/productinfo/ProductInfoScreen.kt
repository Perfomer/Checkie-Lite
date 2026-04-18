package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.cui.modifier.ShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.shake
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiSuggestionsBox
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.focusedFieldScrollContainer
import com.perfomer.checkielite.common.ui.util.focusedFieldScrollTarget
import com.perfomer.checkielite.common.ui.util.rememberFocusedFieldScroller
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.common.ui.util.resource.text.textOrNull
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.widget.CurrencySymbol
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.LocalObstruction
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input.DecimalInputFilter
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input.DecimalInputVisualTransformation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.widget.ProductInfoPhotoSection
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductInfoScreen(
    state: ProductInfoPageUiState,
    scrollState: ScrollState = rememberScrollState(),
    priceFocusRequester: FocusRequester = remember { FocusRequester() },
    productNameFocusRequester: FocusRequester = remember { FocusRequester() },
    productNameShakeController: ShakeController = rememberShakeController(),
    onProductNameTextInput: (String) -> Unit = {},
    onBrandTextInput: (String) -> Unit = {},
    onPriceTextInput: (String) -> Unit = {},
    onPriceCurrencyClick: () -> Unit = {},
    onAddPictureClick: () -> Unit = {},
    onTakePhotoClick: () -> Unit = {},
    onPictureClick: (position: Int) -> Unit = {},
    onPictureDeleteClick: (position: Int) -> Unit = {},
    onPictureReorder: (pictureId: String, toPosition: Int) -> Unit = { _, _ -> },
) {
    val obstruction = LocalObstruction.current
    val brandNameInteractionSource = remember { MutableInteractionSource() }
    val decimalInputFilter = remember { DecimalInputFilter() }
    val fieldScroller = rememberFocusedFieldScroller<String>(
        scrollState = scrollState,
        bottomObstruction = obstruction.calculateBottomPadding(),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusedFieldScrollContainer(fieldScroller)
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .imePadding()
            .padding(top = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_productinfo_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalCuiPalette.current.TextPrimary,
            modifier = Modifier.padding(horizontal = ProductInfoContentPadding)
        )

        CuiSpacer(22.dp)

        ProductInfoPhotoSection(
            picturesUri = state.picturesUri,
            onAddPictureClick = onAddPictureClick,
            onTakePhotoClick = onTakePhotoClick,
            onPictureClick = onPictureClick,
            onPictureDeleteClick = onPictureDeleteClick,
            onPictureReorder = onPictureReorder,
        )

        CuiSpacer(16.dp)

        Column(modifier = Modifier.padding(horizontal = ProductInfoContentPadding)) {
            CuiOutlinedField(
                text = state.productName,
                errorText = textOrNull(state.productNameErrorText),
                reservePlaceForError = true,
                title = stringResource(R.string.reviewcreation_productinfo_field_product),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                onValueChange = onProductNameTextInput,
                modifier = Modifier
                    .focusRequester(productNameFocusRequester)
                    .focusedFieldScrollTarget("productName", fieldScroller)
                    .shake(productNameShakeController),
            )

            CuiSuggestionsBox(
                currentValue = state.brand,
                suggestions = state.brandSuggestions,
                contentInteractionSource = brandNameInteractionSource,
                onSuggestionSelected = onBrandTextInput,
            ) {
                CuiOutlinedField(
                    text = state.brand,
                    title = stringResource(R.string.reviewcreation_productinfo_field_brand),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    onValueChange = onBrandTextInput,
                    interactionSource = brandNameInteractionSource,
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                        .focusedFieldScrollTarget("brand", fieldScroller),
                )
            }

            CuiSpacer(4.dp)

            CuiOutlinedField(
                text = state.price,
                title = stringResource(R.string.reviewcreation_productinfo_field_price),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Decimal,
                ),
                onValueChange = { value ->
                    onPriceTextInput(decimalInputFilter.cleanUp(value))
                },
                visualTransformation = remember { DecimalInputVisualTransformation() },
                trailingIcon = {
                    CurrencySymbol(
                        currencySymbol = text(state.priceCurrency),
                        onClick = onPriceCurrencyClick,
                        modifier = Modifier
                            .requiredWidthIn(min = 156.dp)
                            .offset(x = (-14).dp),
                    )
                },
                modifier = Modifier
                    .focusRequester(priceFocusRequester)
                    .focusedFieldScrollTarget("price", fieldScroller),
            )

            CuiSpacer(obstruction.calculateBottomPadding() + 24.dp)
        }
    }
}

@Composable
@ScreenPreview
private fun ProductInfoScreenPreview() = CheckieLiteTheme {
    ProductInfoScreen(
        state = ProductInfoPageUiState(
            productName = "Aboba",
            productNameErrorText = null,
            brand = "Abobov",
            brandSuggestions = emptyPersistentList(),
            price = "0",
            priceCurrency = Text.raw("RUB"),
            picturesUri = persistentListOf(
                ProductInfoPageUiState.Picture(id = "1", uri = "https://example.com/1"),
            ),
        ),
    )
}
