package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import coil.compose.AsyncImage
import com.mohamedrejeb.compose.dnd.reorder.ReorderContainer
import com.mohamedrejeb.compose.dnd.reorder.ReorderableItem
import com.mohamedrejeb.compose.dnd.reorder.rememberReorderState
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.pure.util.move
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.shake
import com.perfomer.checkielite.common.ui.cui.widget.dropdown.CuiSuggestionsBox
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.widget.CurrencySymbol
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input.DecimalInputFilter
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.input.DecimalInputVisualTransformation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductInfoScreen(
    state: ProductInfoPageUiState,

    scrollState: ScrollState = rememberScrollState(),

    priceFocusRequester: FocusRequester = remember { FocusRequester() },
    shouldCollapseProductNameField: Boolean = false,

    onProductNameFieldCollapsed: () -> Unit = {},
    onProductNameTextInput: (String) -> Unit = {},
    onBrandTextInput: (String) -> Unit = {},
    onPriceTextInput: (String) -> Unit = {},
    onPriceCurrencyClick: () -> Unit = {},
    onAddPictureClick: () -> Unit = {},
    onPictureClick: (position: Int) -> Unit = {},
    onPictureDeleteClick: (position: Int) -> Unit = {},
    onPictureReorder: (pictureUri: String, toPosition: Int) -> Unit = { _, _ -> },
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

        Spacer(Modifier.height(16.dp))

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
            modifier = Modifier.shake(shouldCollapseProductNameField, onProductNameFieldCollapsed)
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

        Spacer(Modifier.height(4.dp))

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

        PicturesFlowRow(
            picturesUri = state.picturesUri,
            onAddPictureClick = onAddPictureClick,
            onPictureClick = onPictureClick,
            onPictureDeleteClick = onPictureDeleteClick,
            onPictureReorder = onPictureReorder,
        )
    }
}

@Composable
private fun PicturesFlowRow(
    picturesUri: ImmutableList<String>,
    onAddPictureClick: () -> Unit,
    onPictureClick: (position: Int) -> Unit,
    onPictureDeleteClick: (position: Int) -> Unit,
    onPictureReorder: (pictureUri: String, toPosition: Int) -> Unit,
) {
    val reorderState = rememberReorderState<String>(dragAfterLongPress = true)
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
            item(
                key = "AddPicture",
                contentType = "AddPicture",
            ) {
                Box {
                    AddPicture(
                        onClick = onAddPictureClick,
                    )
                }
            }

            itemsIndexed(
                items = reorderableItems,
                key = { _, uri -> uri },
                contentType = { _, _ -> "Picture" },
            ) { i, pictureUri ->
                ReorderableItem(
                    state = reorderState,
                    key = i,
                    data = pictureUri,
                    onDragEnter = { state -> reorderableItems = reorderableItems.move(item = state.data, toPosition = i).toPersistentList() },
                    onDrop = { draggedItem -> onPictureReorder(draggedItem.data, i) },
                    draggableContent = {
                        Picture(
                            pictureUrl = pictureUri,
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
                            pictureUrl = pictureUri,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = CommonDrawable.ic_add_picture),
        contentDescription = null,
        tint = LocalCuiPalette.current.IconAccent,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LocalCuiPalette.current.BackgroundAccentSecondary)
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
            picturesUri = persistentListOf(
                "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
                "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
                "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"
            )
        )
    )
}