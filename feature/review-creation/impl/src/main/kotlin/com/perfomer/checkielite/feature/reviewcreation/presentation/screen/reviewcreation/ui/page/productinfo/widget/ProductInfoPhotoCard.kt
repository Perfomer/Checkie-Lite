package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoPhotoDeleteButtonAnimationDuration
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoPhotoDeleteButtonAnimationScale
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoPhotoShape

@Composable
internal fun PhotoPlaceholderCard(
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
            ),
    ) {
        Icon(
            painter = painterResource(id = CommonDrawable.ic_image),
            contentDescription = null,
            tint = palette.IconQuaternary.copy(alpha = 0.2F),
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
internal fun PhotoCard(
    pictureUrl: String,
    position: Int,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isDeleteButtonVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val outlineColor = if (isSystemInDarkTheme()) {
        palette.OutlinePicture
    } else {
        palette.BackgroundPrimary.copy(alpha = 0.8F)
    }
    val badgeText = remember(position) { (position + 1).toString().padStart(2, '0') }

    Box(modifier = modifier) {
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
                .clickable(onClick = onClick),
        ) {
            AsyncImage(
                model = pictureUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp),
            ) {
                PicturePositionBadge(text = badgeText)
            }

            DragHandleBadge(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
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
                .offset(x = 8.dp, y = (-8).dp),
        ) {
            DeleteIconButton(onClick = onDeleteClick)
        }
    }
}

@Composable
internal fun PhotoCountBadge(
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
                    ),
                ),
            )
            .border(
                width = 1.dp,
                color = palette.OutlineAccentSecondary.copy(alpha = 0.45f),
                shape = CircleShape,
            ),
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
            .alpha(0.7F),
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
            .padding(horizontal = 10.dp, vertical = 7.dp),
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(palette.IconSecondary),
            )
        }
    }
}

@Composable
private fun DeleteIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val backgroundColor = if (isSystemInDarkTheme()) {
        palette.BackgroundSecondary
    } else {
        palette.BackgroundPrimary
    }

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
            .clickable(onClick = onClick),
    ) {
        Icon(
            painter = painterResource(CommonDrawable.ic_cross),
            contentDescription = null,
            tint = palette.IconPrimary,
            modifier = Modifier.size(14.dp),
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
