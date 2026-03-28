package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
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

private val ProductInfoContentPadding = 24.dp
private val ProductInfoCarouselSpacing = 12.dp
private val ProductInfoPhotoShape = RoundedCornerShape(30.dp)
private const val ProductInfoCarouselItemAspectRatio = 0.68f
private const val ProductInfoVisibleItems = 3

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
            .padding(top = 24.dp, bottom = 104.dp)
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
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
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
        }
    }
}

@Composable
private fun ProductInfoPhotoSection(
    picturesUri: ImmutableList<ProductInfoPageUiState.Picture>,
    onAddPictureClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPictureClick: (position: Int) -> Unit,
    onPictureDeleteClick: (position: Int) -> Unit,
    onPictureReorder: (pictureId: String, toPosition: Int) -> Unit,
) {
    Column {
        ProductInfoPhotoHeader(
            picturesCount = picturesUri.size,
            modifier = Modifier.padding(horizontal = ProductInfoContentPadding)
        )

        CuiSpacer(14.dp)

        ProductInfoPhotoCarousel(
            picturesUri = picturesUri,
            onAddPictureClick = onAddPictureClick,
            onTakePhotoClick = onTakePhotoClick,
            onPictureClick = onPictureClick,
            onPictureDeleteClick = onPictureDeleteClick,
            onPictureReorder = onPictureReorder,
        )
    }
}

@Composable
private fun ProductInfoPhotoHeader(
    picturesCount: Int,
    modifier: Modifier = Modifier,
) {
    val subtitleRes = if (picturesCount == 0) {
        R.string.reviewcreation_productinfo_photos_subtitle_empty
    } else {
        R.string.reviewcreation_productinfo_photos_subtitle_sort
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.reviewcreation_productinfo_section_photos),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCuiPalette.current.TextPrimary,
            )

            Text(
                text = stringResource(subtitleRes),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = LocalCuiPalette.current.TextSecondary,
            )
        }

        if (picturesCount > 0) {
            PhotoCountBadge(count = picturesCount)
        }
    }
}

@Composable
private fun ProductInfoPhotoCarousel(
    picturesUri: ImmutableList<ProductInfoPageUiState.Picture>,
    onAddPictureClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPictureClick: (position: Int) -> Unit,
    onPictureDeleteClick: (position: Int) -> Unit,
    onPictureReorder: (pictureId: String, toPosition: Int) -> Unit,
) {
    val reorderState = rememberReorderState<ProductInfoPageUiState.Picture>(dragAfterLongPress = true)
    val hapticFeedback = LocalHapticFeedback.current

    var reorderableItems by remember(picturesUri) { mutableStateOf(picturesUri) }
    val scrollableState = rememberLazyListState()

    val currentDraggedPosition by remember { derivedStateOf { reorderState.draggedItem?.key as? Int } }
    val targetDraggedPosition by remember { derivedStateOf { reorderState.hoveredDropTargetKey as? Int } }
    val hiddenItemPosition by remember { derivedStateOf { targetDraggedPosition ?: currentDraggedPosition } }
    val isDragging by remember { derivedStateOf { reorderState.draggedItem != null } }
    val scrollAllowed = picturesUri.size > ProductInfoVisibleItems - 1

    UpdateEffect(isDragging) {
        if (isDragging) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    UpdateEffect(scrollAllowed) {
        if (!scrollAllowed) scrollableState.animateScrollToItem(0)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val itemWidth = (
            maxWidth -
                ProductInfoContentPadding * 2 -
                ProductInfoCarouselSpacing * (ProductInfoVisibleItems - 1)
            ) / ProductInfoVisibleItems
        val itemHeight = itemWidth / ProductInfoCarouselItemAspectRatio
        val placeholderCount = maxOf(0, ProductInfoVisibleItems - reorderableItems.size)
        val rowVerticalPadding = 8.dp

        ReorderContainer(
            state = reorderState,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                state = scrollableState,
                userScrollEnabled = !isDragging && scrollAllowed,
                contentPadding = PaddingValues(
                    start = ProductInfoContentPadding,
                    end = ProductInfoContentPadding,
                    top = rowVerticalPadding,
                    bottom = rowVerticalPadding,
                ),
                horizontalArrangement = Arrangement.spacedBy(ProductInfoCarouselSpacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight + rowVerticalPadding * 2),
            ) {
                item(key = "photo-actions") {
                    PhotoActionColumn(
                        onAddPictureClick = onAddPictureClick,
                        onTakePhotoClick = onTakePhotoClick,
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight)
                    )
                }

                itemsIndexed(
                    items = reorderableItems,
                    key = { _, picture -> picture.id },
                ) { index, picture ->
                    ReorderableItem(
                        state = reorderState,
                        key = index,
                        data = picture,
                        onDragEnter = { state ->
                            reorderableItems = reorderableItems.move(item = state.data, toPosition = index).toPersistentList()
                        },
                        onDrop = { draggedItem ->
                            onPictureReorder(draggedItem.data.id, index)
                        },
                        draggableContent = {
                            PhotoCard(
                                pictureUrl = picture.uri,
                                position = index,
                                onClick = {},
                                onDeleteClick = null,
                                modifier = Modifier
                                    .width(itemWidth)
                                    .height(itemHeight)
                            )
                        },
                        modifier = Modifier.animateItem()
                    ) {
                        PhotoCard(
                            pictureUrl = picture.uri,
                            position = index,
                            onClick = { onPictureClick(index) },
                            onDeleteClick = { onPictureDeleteClick(index) },
                            modifier = Modifier
                                .width(itemWidth)
                                .height(itemHeight)
                                .graphicsLayer { alpha = if (key == hiddenItemPosition) 0f else 1f }
                        )
                    }
                }

                items(count = placeholderCount, key = { placeholderIndex -> "placeholder-$placeholderIndex" }) {
                    PhotoPlaceholderCard(
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight)
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoActionColumn(
    onAddPictureClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        PhotoActionButton(
            title = stringResource(R.string.reviewcreation_productinfo_action_gallery),
            painter = painterResource(id = CommonDrawable.ic_add_picture_v2),
            onClick = onAddPictureClick,
            modifier = Modifier.weight(1f)
        )

        PhotoActionButton(
            title = stringResource(R.string.reviewcreation_productinfo_action_camera),
            painter = painterResource(id = R.drawable.ic_camera),
            onClick = onTakePhotoClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PhotoActionButton(
    title: String,
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(ProductInfoPhotoShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.BackgroundAccentSecondary.copy(alpha = 0.2F),
                        palette.BackgroundAccentTertiary.copy(alpha = 0.95F),
                    )
                )
            )
            .border(
                width = 1.dp,
                color = palette.OutlineAccentSecondary.copy(alpha = 0.7F),
                shape = ProductInfoPhotoShape,
            )
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(CircleShape)
                .background(palette.BackgroundPrimary.copy(alpha = 0.7F))
                .padding(10.dp)
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = palette.IconAccent,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.TextAccent,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PhotoPlaceholderCard(
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(ProductInfoPhotoShape)
            .productInfoDashedBorder(
                color = palette.OutlineSecondary,
                cornerRadius = 30.dp,
            )
    ) {
        Icon(
            painter = painterResource(id = CommonDrawable.ic_image),
            contentDescription = null,
            tint = palette.IconQuaternary.copy(alpha = 0.2F),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun PhotoCard(
    pictureUrl: String,
    position: Int,
    onClick: () -> Unit,
    onDeleteClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val outlineColor = if (isSystemInDarkTheme()) palette.OutlinePicture else palette.BackgroundPrimary.copy(alpha = 0.8F)
    val badgeText = remember(position) { (position + 1).toString().padStart(2, '0') }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = palette.MediumElevation,
                    shape = ProductInfoPhotoShape,
                )
                .clip(ProductInfoPhotoShape)
                .background(palette.BackgroundSecondary)
                .border(
                    width = 1.dp,
                    color = outlineColor,
                    shape = ProductInfoPhotoShape,
                )
                .clickable(onClick = onClick)
        ) {
            AsyncImage(
                model = pictureUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
            ) {
                PicturePositionBadge(text = badgeText)
            }

            DragHandleBadge(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }

        if (onDeleteClick != null) {
            DeleteIconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-8).dp)
            )
        }
    }
}

@Composable
private fun PicturePositionBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(LocalCuiPalette.current.BackgroundPrimary)
            .alpha(0.7F)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.TextPrimary,
        )
    }
}

@Composable
private fun DragHandleBadge(
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(palette.BackgroundPrimary.copy(alpha = 0.94f))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(palette.IconSecondary)
            )
        }
    }
}

@Composable
private fun PhotoCountBadge(
    count: Int,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val badgeText = remember(count) { count.toString().padStart(2, '0') }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        palette.BackgroundPrimary.copy(alpha = 0.84f),
                        palette.BackgroundAccentTertiary.copy(alpha = 0.84f),
                    )
                )
            )
            .border(
                width = 1.dp,
                color = palette.OutlineAccentSecondary.copy(alpha = 0.45f),
                shape = CircleShape,
            )
    ) {
        Text(
            text = badgeText,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.TextAccent,
        )
    }
}

@Composable
private fun DeleteIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val backgroundColor = if (isSystemInDarkTheme()) palette.BackgroundSecondary else palette.BackgroundPrimary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor.copy(alpha = 0.92F))
            .border(
                width = 1.dp,
                color = palette.OutlineSecondary.copy(alpha = 0.5F),
                shape = CircleShape,
            )
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(CommonDrawable.ic_cross),
            contentDescription = null,
            tint = palette.IconPrimary,
            modifier = Modifier.size(14.dp)
        )
    }
}

private fun Modifier.productInfoDashedBorder(
    color: Color,
    cornerRadius: Dp,
): Modifier = drawWithCache {
    val stroke = Stroke(
        width = 1.5.dp.toPx(),
        cap = StrokeCap.Round,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(16.dp.toPx(), 12.dp.toPx()),
        ),
    )
    val radius = cornerRadius.toPx()

    onDrawWithContent {
        drawContent()
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(radius, radius),
            style = stroke,
        )
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
            priceCurrency = "RUB",
            picturesUri = persistentListOf(
                ProductInfoPageUiState.Picture(id = "1", uri = "https://example.com/1"),
            ),
        )
    )
}
