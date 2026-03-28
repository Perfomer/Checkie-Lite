package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
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
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlin.math.roundToInt

private val ProductInfoContentPadding = 24.dp
private val ProductInfoCarouselSpacing = 12.dp
private val ProductInfoPhotoShape = RoundedCornerShape(30.dp)
private val ProductInfoPhotoDragScale = 1.04f
private val ProductInfoPhotoReorderAutoScrollThreshold = 72.dp
private val ProductInfoPhotoReorderAutoScrollMaxSpeed = 360.dp
private val ProductInfoPhotoReorderAutoScrollActivationDistance = 18.dp
private val ProductInfoPhotoDeleteButtonAnimationDuration = 110
private const val ProductInfoPhotoDeleteButtonAnimationScale = 0.92f
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
    val scrollableState = rememberLazyListState()
    val dragState = rememberProductInfoPhotoDragState()
    val hapticFeedback = LocalHapticFeedback.current

    var reorderableItems by remember { mutableStateOf(picturesUri) }
    var isAwaitingExternalSync by remember { mutableStateOf(false) }
    var pendingScrollToEnd by remember { mutableStateOf(false) }
    var containerLeftInRoot by remember { mutableFloatStateOf(0f) }
    var containerTopInRoot by remember { mutableFloatStateOf(0f) }
    val scrollAllowed = reorderableItems.size > ProductInfoVisibleItems - 1
    val isInteractionActive = dragState.isDragging || dragState.isSettling
    val currentReorderableItems by rememberUpdatedState(newValue = reorderableItems)
    val currentOnPictureReorder by rememberUpdatedState(newValue = onPictureReorder)
    val currentIsInteractionActive by rememberUpdatedState(newValue = isInteractionActive)
    val currentContainerLeftInRoot by rememberUpdatedState(newValue = containerLeftInRoot)
    val currentContainerTopInRoot by rememberUpdatedState(newValue = containerTopInRoot)

    UpdateEffect(picturesUri) {
        when {
            !reorderableItems.hasSamePicturePool(picturesUri) -> {
                pendingScrollToEnd = picturesUri.size > reorderableItems.size && scrollAllowed
                reorderableItems = picturesUri
                isAwaitingExternalSync = false
                dragState.resetImmediately()
            }

            !isInteractionActive -> {
                when {
                    reorderableItems == picturesUri -> {
                        isAwaitingExternalSync = false
                    }

                    !isAwaitingExternalSync && reorderableItems != picturesUri -> {
                        reorderableItems = picturesUri
                    }
                }
            }
        }
    }

    UpdateEffect(scrollAllowed) {
        if (!scrollAllowed) {
            dragState.stopAutoScroll()
            scrollableState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(
        pendingScrollToEnd,
        reorderableItems.size,
        isInteractionActive,
    ) {
        if (!pendingScrollToEnd || isInteractionActive || reorderableItems.isEmpty()) {
            return@LaunchedEffect
        }

        withFrameNanos { }
        scrollableState.animateScrollToItem(index = reorderableItems.lastIndex + 1)
        pendingScrollToEnd = false
    }

    LaunchedEffect(dragState.isDragging, scrollableState, scrollAllowed) {
        if (!dragState.isDragging || !scrollAllowed) {
            dragState.stopAutoScroll()
            return@LaunchedEffect
        }

        var previousFrameNanos = 0L

        while (dragState.isDragging && scrollAllowed && currentCoroutineContext().isActive) {
            val frameNanos = withFrameNanos { it }
            val frameDeltaSeconds = if (previousFrameNanos == 0L) {
                1f / 60f
            } else {
                ((frameNanos - previousFrameNanos) / 1_000_000_000f).coerceAtLeast(1f / 120f)
            }
            previousFrameNanos = frameNanos

            val autoScrollDelta = dragState.autoScrollVelocityPxPerSecond * frameDeltaSeconds
            if (autoScrollDelta == 0f) continue

            val consumedScroll = scrollableState.scrollBy(autoScrollDelta)
            if (consumedScroll == 0f) continue

            val update = dragState.onScroll(currentReorderableItems)
            if (update.pictures != currentReorderableItems) {
                reorderableItems = update.pictures
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                containerLeftInRoot = coordinates.positionInRoot().x
                containerTopInRoot = coordinates.positionInRoot().y
            }
    ) {
        val density = LocalDensity.current
        val itemWidth = (
            maxWidth -
                ProductInfoContentPadding * 2 -
                ProductInfoCarouselSpacing * (ProductInfoVisibleItems - 1)
            ) / ProductInfoVisibleItems
        val itemHeight = itemWidth / ProductInfoCarouselItemAspectRatio
        val placeholderCount = maxOf(0, ProductInfoVisibleItems - reorderableItems.size)
        val rowVerticalPadding = 8.dp
        val rowVerticalPaddingPx = with(density) { rowVerticalPadding.roundToPx() }
        val contentStartPx = with(density) { ProductInfoContentPadding.toPx() }
        val contentEndPx = with(density) { maxWidth.toPx() - ProductInfoContentPadding.toPx() }
        val floatingPictureId = dragState.floatingPictureId
        val floatingPicture = remember(floatingPictureId, reorderableItems) {
            reorderableItems.firstOrNull { it.id == floatingPictureId }
        }
        val floatingPicturePosition = remember(floatingPictureId, reorderableItems) {
            reorderableItems.indexOfFirst { it.id == floatingPictureId }
        }
        val floatingPictureOffsetX = remember { Animatable(0f) }
        val floatingPictureScale by animateFloatAsState(
            targetValue = if (floatingPictureId != null) ProductInfoPhotoDragScale else 1f,
            label = "ProductInfoFloatingPhotoScale",
        )
        val floatingPictureLeftPx = if (dragState.isDragging) {
            dragState.overlayLeftPx
        } else {
            floatingPictureOffsetX.value
        }

        dragState.updateViewport(
            contentStartPx = containerLeftInRoot + contentStartPx,
            contentEndPx = containerLeftInRoot + contentEndPx,
        )

        LaunchedEffect(
            dragState.isDragging,
            dragState.overlayLeftPx,
            dragState.isSettling,
            dragState.settlingFromLeftPx,
            dragState.settlingToLeftPx,
        ) {
            when {
                dragState.isDragging -> {
                    floatingPictureOffsetX.snapTo(dragState.overlayLeftPx)
                }

                dragState.isSettling -> {
                    floatingPictureOffsetX.snapTo(dragState.settlingFromLeftPx)
                    floatingPictureOffsetX.animateTo(
                        targetValue = dragState.settlingToLeftPx,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                    )
                    dragState.onSettlingFinished()
                }

                else -> {
                    if (floatingPictureOffsetX.value != 0f) floatingPictureOffsetX.snapTo(0f)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            if (currentReorderableItems.size < 2 || currentIsInteractionActive) {
                                return@detectDragGesturesAfterLongPress
                            }

                            val pictureIndex = dragState.findPictureIndexAtPosition(
                                pictures = currentReorderableItems,
                                touchX = currentContainerLeftInRoot + offset.x,
                                touchY = currentContainerTopInRoot + offset.y,
                            ) ?: return@detectDragGesturesAfterLongPress

                            val didStart = dragState.startDrag(
                                pictureId = currentReorderableItems[pictureIndex].id,
                                pictureIndex = pictureIndex,
                            )
                            if (!didStart) return@detectDragGesturesAfterLongPress

                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDragEnd = {
                            dragState.finishDrag(currentReorderableItems)?.let { dropResult ->
                                isAwaitingExternalSync = true
                                currentOnPictureReorder(dropResult.pictureId, dropResult.toPosition)
                            }
                        },
                        onDragCancel = {
                            dragState.cancelDrag(currentReorderableItems)?.let { dropResult ->
                                isAwaitingExternalSync = true
                                currentOnPictureReorder(dropResult.pictureId, dropResult.toPosition)
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            val update = dragState.onDrag(
                                pictures = currentReorderableItems,
                                dragDeltaX = dragAmount.x,
                            )

                            if (update.pictures != currentReorderableItems) {
                                reorderableItems = update.pictures
                            }
                        },
                    )
                }
        ) {
            LazyRow(
                state = scrollableState,
                userScrollEnabled = !isInteractionActive && scrollAllowed,
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
                    val isHidden = picture.id == floatingPictureId
                    val itemModifier = if (isHidden) {
                        Modifier
                    } else {
                        Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null,
                            placementSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                        )
                    }

                    PhotoCard(
                        pictureUrl = picture.uri,
                        position = index,
                        onClick = { if (!dragState.isDragging) onPictureClick(index) },
                        onDeleteClick = { onPictureDeleteClick(index) },
                        isDeleteButtonVisible = !dragState.isDragging,
                        modifier = itemModifier
                            .width(itemWidth)
                            .height(itemHeight)
                            .onGloballyPositioned { coordinates ->
                                dragState.onItemMeasured(
                                    pictureId = picture.id,
                                    leftPx = coordinates.positionInRoot().x,
                                    topPx = coordinates.positionInRoot().y,
                                    widthPx = coordinates.size.width.toFloat(),
                                    heightPx = coordinates.size.height.toFloat(),
                                )
                            }
                            .graphicsLayer {
                                alpha = if (isHidden) 0f else 1f
                            },
                        contentModifier = Modifier,
                    )
                }

                items(count = placeholderCount, key = { placeholderIndex -> "placeholder-$placeholderIndex" }) {
                    PhotoPlaceholderCard(
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight)
                    )
                }
            }

            if (floatingPicture != null && floatingPicturePosition != -1) {
                PhotoCard(
                    pictureUrl = floatingPicture.uri,
                    position = floatingPicturePosition,
                    onClick = {},
                    onDeleteClick = {},
                    isDeleteButtonVisible = false,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (floatingPictureLeftPx - containerLeftInRoot).roundToInt(),
                                y = rowVerticalPaddingPx,
                            )
                        }
                        .width(itemWidth)
                        .height(itemHeight)
                        .zIndex(2f)
                        .graphicsLayer {
                            scaleX = floatingPictureScale
                            scaleY = floatingPictureScale
                        },
                )
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
    onDeleteClick: () -> Unit,
    isDeleteButtonVisible: Boolean,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val outlineColor = if (isSystemInDarkTheme()) palette.OutlinePicture else palette.BackgroundPrimary.copy(alpha = 0.8F)
    val badgeText = remember(position) { (position + 1).toString().padStart(2, '0') }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = contentModifier
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

        AnimatedVisibility(
            visible = isDeleteButtonVisible,
            enter = fadeIn(
                animationSpec = tween(ProductInfoPhotoDeleteButtonAnimationDuration),
            ) + scaleIn(
                initialScale = ProductInfoPhotoDeleteButtonAnimationScale,
                animationSpec = tween(ProductInfoPhotoDeleteButtonAnimationDuration),
            ),
            exit = fadeOut(
                animationSpec = tween(ProductInfoPhotoDeleteButtonAnimationDuration),
            ) + scaleOut(
                targetScale = ProductInfoPhotoDeleteButtonAnimationScale,
                animationSpec = tween(ProductInfoPhotoDeleteButtonAnimationDuration),
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-8).dp)
        ) {
            DeleteIconButton(
                onClick = onDeleteClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun rememberProductInfoPhotoDragState(): ProductInfoPhotoDragState {
    val density = LocalDensity.current
    val autoScrollThresholdPx = with(density) { ProductInfoPhotoReorderAutoScrollThreshold.toPx() }
    val autoScrollMaxSpeedPxPerSecond = with(density) { ProductInfoPhotoReorderAutoScrollMaxSpeed.toPx() }
    val autoScrollActivationDistancePx = with(density) { ProductInfoPhotoReorderAutoScrollActivationDistance.toPx() }
    val itemSpacingPx = with(density) { ProductInfoCarouselSpacing.toPx() }

    return remember(
        autoScrollThresholdPx,
        autoScrollMaxSpeedPxPerSecond,
        autoScrollActivationDistancePx,
        itemSpacingPx,
    ) {
        ProductInfoPhotoDragState(
            autoScrollThresholdPx = autoScrollThresholdPx,
            autoScrollMaxSpeedPxPerSecond = autoScrollMaxSpeedPxPerSecond,
            autoScrollActivationDistancePx = autoScrollActivationDistancePx,
            itemSpacingPx = itemSpacingPx,
        )
    }
}

@Stable
private class ProductInfoPhotoDragState(
    private val autoScrollThresholdPx: Float,
    private val autoScrollMaxSpeedPxPerSecond: Float,
    private val autoScrollActivationDistancePx: Float,
    private val itemSpacingPx: Float,
) {
    private val itemBounds = mutableStateMapOf<String, ProductInfoPhotoItemBounds>()

    private var contentStartPx by mutableFloatStateOf(0f)
    private var contentEndPx by mutableFloatStateOf(0f)

    var draggedPictureId by mutableStateOf<String?>(null)
        private set

    var settlingPictureId by mutableStateOf<String?>(null)
        private set

    private var initialDraggedPictureIndex by mutableIntStateOf(-1)

    var draggedPictureIndex by mutableIntStateOf(-1)
        private set

    var overlayLeftPx by mutableFloatStateOf(0f)
        private set

    private var dragDisplacementX by mutableFloatStateOf(0f)

    var settlingFromLeftPx by mutableFloatStateOf(0f)
        private set

    var settlingToLeftPx by mutableFloatStateOf(0f)
        private set

    var autoScrollVelocityPxPerSecond by mutableFloatStateOf(0f)
        private set

    val floatingPictureId: String?
        get() = draggedPictureId ?: settlingPictureId

    val isDragging: Boolean
        get() = draggedPictureId != null

    val isSettling: Boolean
        get() = settlingPictureId != null

    fun updateViewport(
        contentStartPx: Float,
        contentEndPx: Float,
    ) {
        this.contentStartPx = contentStartPx
        this.contentEndPx = contentEndPx
    }

    fun onItemMeasured(
        pictureId: String,
        leftPx: Float,
        topPx: Float,
        widthPx: Float,
        heightPx: Float,
    ) {
        itemBounds[pictureId] = ProductInfoPhotoItemBounds(
            leftPx = leftPx,
            topPx = topPx,
            widthPx = widthPx,
            heightPx = heightPx,
        )
    }

    fun findPictureIndexAtPosition(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
        touchX: Float,
        touchY: Float,
    ): Int? {
        val pictureIndex = pictures.indexOfFirst { picture ->
            itemBounds[picture.id]?.contains(
                xPx = touchX,
                yPx = touchY,
            ) == true
        }

        return pictureIndex.takeIf { it >= 0 }
    }

    fun startDrag(
        pictureId: String,
        pictureIndex: Int,
    ): Boolean {
        if (isDragging) return false

        val bounds = itemBounds[pictureId] ?: return false

        settlingPictureId = null
        settlingFromLeftPx = 0f
        settlingToLeftPx = 0f
        draggedPictureId = pictureId
        initialDraggedPictureIndex = pictureIndex
        draggedPictureIndex = pictureIndex
        overlayLeftPx = bounds.leftPx
        dragDisplacementX = 0f
        recalculateAutoScroll()
        return true
    }

    fun onDrag(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
        dragDeltaX: Float,
    ): ProductInfoPhotoReorderUpdate {
        if (!isDragging) return ProductInfoPhotoReorderUpdate(pictures)

        overlayLeftPx += dragDeltaX
        dragDisplacementX += dragDeltaX
        return moveIfNeeded(pictures)
    }

    fun onScroll(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
    ): ProductInfoPhotoReorderUpdate {
        if (!isDragging) return ProductInfoPhotoReorderUpdate(pictures)

        return moveIfNeeded(pictures)
    }

    fun finishDrag(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
    ): ProductInfoPhotoDropResult? {
        return startSettling(pictures)
    }

    fun cancelDrag(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
    ): ProductInfoPhotoDropResult? {
        return startSettling(pictures)
    }

    fun resetImmediately() {
        reset()
    }

    fun onSettlingFinished() {
        settlingPictureId = null
        settlingFromLeftPx = 0f
        settlingToLeftPx = 0f
        overlayLeftPx = 0f
    }

    fun stopAutoScroll() {
        autoScrollVelocityPxPerSecond = 0f
    }

    private fun moveIfNeeded(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
    ): ProductInfoPhotoReorderUpdate {
        val draggedId = draggedPictureId ?: return ProductInfoPhotoReorderUpdate(pictures)
        val currentIndex = resolveDraggedIndex(pictures, draggedId) ?: return ProductInfoPhotoReorderUpdate(pictures)
        val currentBounds = itemBounds[draggedId] ?: return ProductInfoPhotoReorderUpdate(pictures).also {
            recalculateAutoScroll()
        }

        val draggedCenter = overlayLeftPx + currentBounds.widthPx / 2f
        val targetIndex = when {
            draggedCenter > currentBounds.centerPx -> {
                val nextIndex = currentIndex + 1
                val nextPictureId = pictures.getOrNull(nextIndex)?.id
                val nextBounds = nextPictureId?.let(itemBounds::get)

                if (nextBounds != null && draggedCenter > nextBounds.centerPx) {
                    nextIndex
                } else {
                    null
                }
            }

            draggedCenter < currentBounds.centerPx -> {
                val previousIndex = currentIndex - 1
                val previousPictureId = pictures.getOrNull(previousIndex)?.id
                val previousBounds = previousPictureId?.let(itemBounds::get)

                if (previousBounds != null && draggedCenter < previousBounds.centerPx) {
                    previousIndex
                } else {
                    null
                }
            }

            else -> null
        } ?: return ProductInfoPhotoReorderUpdate(pictures).also {
            recalculateAutoScroll()
        }

        draggedPictureIndex = targetIndex
        recalculateAutoScroll()

        return ProductInfoPhotoReorderUpdate(
            pictures = pictures
                .move(item = pictures[currentIndex], toPosition = targetIndex)
                .toPersistentList(),
        )
    }

    private fun resolveDraggedIndex(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
        draggedId: String,
    ): Int? {
        val currentIndex = draggedPictureIndex
        if (currentIndex in pictures.indices && pictures[currentIndex].id == draggedId) {
            return currentIndex
        }

        val resolvedIndex = pictures.indexOfFirst { it.id == draggedId }
        if (resolvedIndex == -1) {
            reset()
            return null
        }

        draggedPictureIndex = resolvedIndex
        return resolvedIndex
    }

    private fun recalculateAutoScroll() {
        val draggedId = draggedPictureId ?: run {
            autoScrollVelocityPxPerSecond = 0f
            return
        }

        val draggedItemBounds = itemBounds[draggedId] ?: run {
            autoScrollVelocityPxPerSecond = 0f
            return
        }

        val draggedItemStart = overlayLeftPx
        val draggedItemEnd = draggedItemStart + draggedItemBounds.widthPx
        val leftBoundary = contentStartPx + autoScrollThresholdPx
        val rightBoundary = contentEndPx - autoScrollThresholdPx

        autoScrollVelocityPxPerSecond = when {
            dragDisplacementX < -autoScrollActivationDistancePx &&
                draggedItemStart < leftBoundary -> {
                -calculateAutoScrollVelocity(leftBoundary - draggedItemStart)
            }

            dragDisplacementX > autoScrollActivationDistancePx &&
                draggedItemEnd > rightBoundary -> {
                calculateAutoScrollVelocity(draggedItemEnd - rightBoundary)
            }

            else -> 0f
        }
    }

    private fun calculateAutoScrollVelocity(
        overshoot: Float,
    ): Float {
        val progress = (overshoot / autoScrollThresholdPx).coerceIn(0f, 1f)
        return autoScrollMaxSpeedPxPerSecond * progress * progress
    }

    private fun reset() {
        settlingPictureId = null
        settlingFromLeftPx = 0f
        settlingToLeftPx = 0f
        clearDragState()
    }

    private fun startSettling(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
    ): ProductInfoPhotoDropResult? {
        val draggedId = draggedPictureId
        val dropResult = if (
            draggedId != null &&
            draggedPictureIndex != -1 &&
            draggedPictureIndex != initialDraggedPictureIndex
        ) {
            ProductInfoPhotoDropResult(
                pictureId = draggedId,
                toPosition = draggedPictureIndex,
            )
        } else {
            null
        }

        if (draggedId != null) {
            val targetOffset = resolveSettlingTargetOffset(
                pictures = pictures,
                draggedId = draggedId,
                targetIndex = draggedPictureIndex,
            )
            settlingPictureId = draggedId
            settlingFromLeftPx = overlayLeftPx
            settlingToLeftPx = targetOffset
        }

        clearDragState()
        return dropResult
    }

    private fun clearDragState() {
        draggedPictureId = null
        initialDraggedPictureIndex = -1
        draggedPictureIndex = -1
        overlayLeftPx = 0f
        dragDisplacementX = 0f
        autoScrollVelocityPxPerSecond = 0f
    }

    private fun resolveSettlingTargetOffset(
        pictures: ImmutableList<ProductInfoPageUiState.Picture>,
        draggedId: String,
        targetIndex: Int,
    ): Float {
        val draggedBounds = itemBounds[draggedId] ?: return overlayLeftPx
        val previousBounds = pictures
            .getOrNull(targetIndex - 1)
            ?.id
            ?.let(itemBounds::get)
        val nextBounds = pictures
            .getOrNull(targetIndex + 1)
            ?.id
            ?.let(itemBounds::get)

        return when {
            previousBounds != null && nextBounds != null -> {
                (previousBounds.leftPx + nextBounds.leftPx) / 2f
            }

            previousBounds != null -> {
                previousBounds.leftPx + previousBounds.widthPx + itemSpacingPx
            }

            nextBounds != null -> {
                nextBounds.leftPx - draggedBounds.widthPx - itemSpacingPx
            }

            else -> draggedBounds.leftPx
        }
    }
}

private data class ProductInfoPhotoItemBounds(
    val leftPx: Float,
    val topPx: Float,
    val widthPx: Float,
    val heightPx: Float,
) {
    val centerPx: Float
        get() = leftPx + widthPx / 2f

    fun contains(
        xPx: Float,
        yPx: Float,
    ): Boolean {
        return xPx in leftPx..(leftPx + widthPx) &&
            yPx in topPx..(topPx + heightPx)
    }
}

private data class ProductInfoPhotoReorderUpdate(
    val pictures: ImmutableList<ProductInfoPageUiState.Picture>,
)

private data class ProductInfoPhotoDropResult(
    val pictureId: String,
    val toPosition: Int,
)

private fun ImmutableList<ProductInfoPageUiState.Picture>.hasSamePicturePool(
    other: ImmutableList<ProductInfoPageUiState.Picture>,
): Boolean {
    if (size != other.size) return false

    return map(ProductInfoPageUiState.Picture::id).toSet() == other.map(ProductInfoPageUiState.Picture::id).toSet()
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
            .border(1.dp, palette.OutlineSecondary.copy(alpha = 0.3F), CircleShape)
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
