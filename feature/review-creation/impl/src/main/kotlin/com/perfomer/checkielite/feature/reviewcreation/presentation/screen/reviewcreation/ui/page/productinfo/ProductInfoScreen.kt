package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mohamedrejeb.compose.dnd.reorder.ReorderContainer
import com.mohamedrejeb.compose.dnd.reorder.ReorderableItem
import com.mohamedrejeb.compose.dnd.reorder.rememberReorderState
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.pure.util.move
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.ShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.shake
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiSuggestionsBox
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.widget.CurrencySymbol
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input.DecimalInputFilter
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input.DecimalInputVisualTransformation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

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
    val brandNameInteractionSource = remember { MutableInteractionSource() }
    val decimalInputFilter = remember { DecimalInputFilter() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .imePadding()
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_productinfo_title),
            fontSize = 24.sp,
            color = LocalCuiPalette.current.TextPrimary,
            fontWeight = FontWeight.Bold,
        )

        CuiSpacer(16.dp)

        CuiOutlinedField(
            text = state.productName,
            errorText = state.productNameErrorText,
            reservePlaceForError = true,
            title = stringResource(R.string.reviewcreation_productinfo_field_product),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            onValueChange = onProductNameTextInput,
            modifier = Modifier
                .focusRequester(productNameFocusRequester)
                .shake(productNameShakeController)
        )

        CuiSuggestionsBox(
            currentValue = state.brand,
            suggestions = state.brandSuggestions,
            contentInteractionSource = brandNameInteractionSource,
            onSuggestionSelected = onBrandTextInput
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
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable)
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
            onValueChange = { value -> onPriceTextInput(decimalInputFilter.cleanUp(value)) },
            visualTransformation = remember { DecimalInputVisualTransformation() },
            trailingIcon = {
                CurrencySymbol(
                    currencySymbol = state.priceCurrency,
                    onClick = onPriceCurrencyClick,
                    modifier = Modifier
                        .requiredWidthIn(min = 156.dp)
                        .offset(x = (-14).dp)
                )
            },
            modifier = Modifier.focusRequester(priceFocusRequester)
        )

        CuiSpacer(12.dp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AddPicture(
                painter = painterResource(id = CommonDrawable.ic_add_picture_v2),
                onClick = onAddPictureClick,
                modifier = Modifier.weight(1F)
            )

            AddPicture(
                painter = painterResource(id = R.drawable.ic_camera),
                onClick = onTakePhotoClick,
                modifier = Modifier.weight(1F)
            )
        }

        CuiSpacer(16.dp)

        PicturesFlowRow(
            picturesUri = state.picturesUri,
            onPictureClick = onPictureClick,
            onPictureDeleteClick = onPictureDeleteClick,
            onPictureReorder = onPictureReorder,
        )
    }
}

@Composable
private fun PicturesFlowRow(
    picturesUri: ImmutableList<ProductInfoPageUiState.Picture>,
    onPictureClick: (position: Int) -> Unit,
    onPictureDeleteClick: (position: Int) -> Unit,
    onPictureReorder: (pictureId: String, toPosition: Int) -> Unit,
) {
    val reorderState = rememberReorderState<ProductInfoPageUiState.Picture>(dragAfterLongPress = true)
    val hapticFeedback = LocalHapticFeedback.current

    var reorderableItems by remember(picturesUri) { mutableStateOf(picturesUri) }

    val currentDraggedPosition by remember { derivedStateOf { reorderState.draggedItem?.key as? Int } }
    val targetDraggedPosition by remember { derivedStateOf { reorderState.hoveredDropTargetKey as? Int } }
    val hiddenItemPosition by remember { derivedStateOf { targetDraggedPosition ?: currentDraggedPosition } }

    val isDragging by remember { derivedStateOf { reorderState.draggedItem != null } }

    UpdateEffect(isDragging) {
        if (isDragging) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    ReorderContainer(
        state = reorderState,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 62.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(top = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                // Need to constraint the height of the grid when it is in scrollable container
                // https://stackoverflow.com/questions/67919707/jetpack-compose-how-to-put-a-lazyverticalgrid-inside-a-scrollable-column
                .heightIn(max = 2000.dp)
        ) {
            itemsIndexed(
                items = reorderableItems,
                key = { _, picture -> picture.id },
            ) { i, picture ->
                ReorderableItem(
                    state = reorderState,
                    key = i,
                    data = picture,
                    onDragEnter = { state ->
                        reorderableItems = reorderableItems.move(item = state.data, toPosition = i).toPersistentList()
                    },
                    onDrop = { draggedItem ->
                        onPictureReorder(draggedItem.data.id, i)
                    },
                    draggableContent = {
                        Picture(
                            pictureUrl = picture.uri,
                            modifier = Modifier.shadow(
                                elevation = LocalCuiPalette.current.MediumElevation,
                                shape = RoundedCornerShape(16.dp),
                            )
                        )
                    },
                    modifier = Modifier.animateItem()
                ) {
                    DeletableItem(
                        onDeletePictureClick = { onPictureDeleteClick(i) },
                        modifier = Modifier.graphicsLayer { alpha = if (key == hiddenItemPosition) 0F else 1F }
                    ) {
                        Picture(
                            pictureUrl = picture.uri,
                            onClick = { onPictureClick(i) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddPicture(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painter,
        contentDescription = null,
        tint = LocalCuiPalette.current.IconAccent,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalCuiPalette.current.BackgroundAccentTertiary)
            .clickable(onClick = onClick)
            .padding(16.dp),
    )
}

@Composable
private fun DeletableItem(
    onDeletePictureClick: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(x = 6.dp, y = (-6).dp),
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        DeleteIconButton(
            onClick = onDeletePictureClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = offset.x, y = offset.y)
        )
    }
}

@Composable
private fun DeleteIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(CommonDrawable.ic_cross),
        tint = LocalCuiPalette.current.IconPrimary,
        contentDescription = null,
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(LocalCuiPalette.current.BackgroundSecondary)
            .clickable(onClick = onClick)
            .padding(3.dp)
    )
}

@Composable
internal fun Picture(
    pictureUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = pictureUrl,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalCuiPalette.current.BackgroundSecondary)
            .clickable(onClick = onClick)
    )
}

@Composable
@ScreenPreview
private fun ProductInfoScreenPreview() {
    ProductInfoScreen(
        state = ProductInfoPageUiState(
            productName = "Aboba",
            productNameErrorText = null,
            brand = "Abobov",
            brandSuggestions = emptyPersistentList(),
            price = "0",
            priceCurrency = "RUB",
            picturesUri = emptyPersistentList(),
        )
    )
}