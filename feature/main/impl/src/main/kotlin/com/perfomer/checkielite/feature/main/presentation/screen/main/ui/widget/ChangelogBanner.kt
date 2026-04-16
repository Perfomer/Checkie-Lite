package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.WhatsNewBanner

private val BannerShape = RoundedCornerShape(24.dp)
private val BadgeShape = RoundedCornerShape(20.dp)
private val PreviewCardShape = RoundedCornerShape(20.dp)
private val PillShape = RoundedCornerShape(100.dp)

private object BannerDimens {
    val OuterHorizontalPadding = 20.dp
    val ContentStartPadding = 18.dp
    val ContentTopPadding = 16.dp
    val ContentEndPadding = 110.dp
    val ContentBottomPadding = 24.dp
    val ContentSpacing = 10.dp
    val TitleSpacing = 4.dp

    val BadgeDotSize = 7.dp
    val BadgeSpacing = 6.dp
    val BadgeHorizontalPadding = 10.dp
    val BadgeVerticalPadding = 6.dp

    val CloseTouchTarget = 36.dp
    val CloseContainerSize = 28.dp
    val CloseIconSize = 14.dp
    val CloseTopPadding = 8.dp
    val CloseEndPadding = 8.dp

    val PreviewOverlayWidth = 108.dp
    val PreviewEndPadding = 8.dp
    val PreviewOffsetX = (-18).dp
    val PreviewOffsetY = (-2).dp

    val TopGlowSize = 148.dp
    val TopGlowOffsetX = (-30).dp
    val TopGlowOffsetY = (-58).dp
    val BottomGlowSize = 124.dp
    val BottomGlowOffsetX = 18.dp
    val BottomGlowOffsetY = 24.dp

    val PreviewHaloSize = 92.dp
    val GhostCardWidth = 58.dp
    val GhostCardHeight = 78.dp
    val GhostCardOffsetX = (-24).dp
    val GhostCardOffsetY = 10.dp
    val GhostCardRotation = -8F
    val GhostCardAlpha = 0.6F

    val FrontCardWidth = 72.dp
    val FrontCardHeight = 94.dp
    val FrontCardRotation = 4F

    val PreviewCardHorizontalPadding = 8.dp
    val PreviewCardVerticalPadding = 7.dp
    val PreviewNotchWidth = 20.dp
    val PreviewNotchHeight = 5.dp
    val PreviewCenterGlowSize = 28.dp
    val PreviewCenterDotSize = 14.dp
    val PreviewLineHeight = 6.dp
    val PreviewSecondaryLineHeight = 5.dp
    val PreviewChipPrimaryWidth = 14.dp
    val PreviewChipSecondaryWidth = 6.dp
    val PreviewChipHeight = 6.dp
    val PreviewChipSpacing = 4.dp
    val PreviewLineSpacing = 5.dp
    val PreviewChipTopSpacing = 7.dp
}

private data class ChangelogBannerColors(
    val backgroundBrush: Brush,
    val borderBrush: Brush,
    val badgeBackgroundColor: Color,
    val badgeBorderColor: Color,
    val badgeTextColor: Color,
    val badgeDotColor: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val closeBackgroundColor: Color,
    val closeTintColor: Color,
    val topGlowColor: Color,
    val bottomGlowColor: Color,
    val previewHaloColor: Color,
)

@Composable
internal fun ChangelogBanner(
    state: WhatsNewBanner,
    onClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current
    val isDarkTheme = isSystemInDarkTheme()
    val colors = changelogBannerColors(
        palette = palette,
        isDarkTheme = isDarkTheme,
    )

    Box(
        modifier = modifier
            .padding(horizontal = BannerDimens.OuterHorizontalPadding)
            .fillMaxWidth()
            .clip(BannerShape)
            .background(colors.backgroundBrush)
            .border(
                width = 1.dp,
                brush = colors.borderBrush,
                shape = BannerShape,
            )
            .clickable(onClick = onClick)
    ) {
        ChangelogBannerBackdrop(colors = colors)

        ChangelogBannerContent(
            state = state,
            colors = colors,
        )

        ChangelogBannerPreviewOverlay(
            palette = palette,
            isDarkTheme = isDarkTheme,
            haloColor = colors.previewHaloColor,
        )

        ChangelogBannerCloseButton(
            backgroundColor = colors.closeBackgroundColor,
            tintColor = colors.closeTintColor,
            onClick = onCloseClick,
        )
    }
}

private fun changelogBannerColors(
    palette: CuiPalette,
    isDarkTheme: Boolean,
): ChangelogBannerColors {
    val backgroundColors = if (isDarkTheme) {
        listOf(
            palette.BackgroundAccentTertiary.copy(alpha = 0.92F),
            palette.BackgroundSecondary,
            palette.BackgroundPrimary,
        )
    } else {
        listOf(
            palette.BackgroundAccentTertiary,
            palette.BackgroundAccentSecondary.copy(alpha = 0.72F),
            palette.BackgroundPrimary,
        )
    }
    val borderColors = listOf(
        palette.OutlineAccentSecondary.copy(alpha = if (isDarkTheme) 0.78F else 0.62F),
        palette.OutlineSecondary.copy(alpha = if (isDarkTheme) 0.56F else 0.82F),
    )

    return ChangelogBannerColors(
        backgroundBrush = Brush.linearGradient(backgroundColors),
        borderBrush = Brush.linearGradient(borderColors),
        badgeBackgroundColor = if (isDarkTheme) {
            palette.BackgroundPrimary.copy(alpha = 0.22F)
        } else {
            palette.BackgroundPrimary.copy(alpha = 0.94F)
        },
        badgeBorderColor = palette.OutlineSecondary.copy(alpha = if (isDarkTheme) 0.46F else 0.72F),
        badgeTextColor = if (isDarkTheme) palette.TextPrimary else palette.TextAccent,
        badgeDotColor = palette.BackgroundAccentPrimary,
        titleColor = palette.TextPrimary,
        subtitleColor = palette.TextSecondary,
        closeBackgroundColor = if (isDarkTheme) {
            palette.BackgroundPrimary.copy(alpha = 0.28F)
        } else {
            palette.BackgroundPrimary.copy(alpha = 0.94F)
        },
        closeTintColor = if (isDarkTheme) palette.IconPrimary else palette.IconSecondary,
        topGlowColor = palette.BackgroundAccentPrimary.copy(alpha = if (isDarkTheme) 0.18F else 0.14F),
        bottomGlowColor = palette.BackgroundAccentSecondary.copy(alpha = if (isDarkTheme) 0.26F else 0.34F),
        previewHaloColor = palette.BackgroundAccentPrimary.copy(alpha = if (isDarkTheme) 0.18F else 0.14F),
    )
}

@Composable
private fun ChangelogBannerContent(
    state: WhatsNewBanner,
    colors: ChangelogBannerColors,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(BannerDimens.ContentSpacing),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = BannerDimens.ContentStartPadding,
                top = BannerDimens.ContentTopPadding,
                end = BannerDimens.ContentEndPadding,
                bottom = BannerDimens.ContentBottomPadding,
            )
    ) {
        ChangelogBannerBadge(colors = colors)

        Column(verticalArrangement = Arrangement.spacedBy(BannerDimens.TitleSpacing)) {
            Text(
                text = state.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                color = colors.titleColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = state.subtitle,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = colors.subtitleColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ChangelogBannerBadge(
    colors: ChangelogBannerColors,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(BannerDimens.BadgeSpacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(BadgeShape)
            .background(colors.badgeBackgroundColor)
            .border(1.dp, colors.badgeBorderColor, BadgeShape)
            .padding(
                horizontal = BannerDimens.BadgeHorizontalPadding,
                vertical = BannerDimens.BadgeVerticalPadding,
            )
    ) {
        Box(
            modifier = Modifier
                .size(BannerDimens.BadgeDotSize)
                .clip(CircleShape)
                .background(colors.badgeDotColor)
        )

        Text(
            text = stringResource(R.string.main_changelog_badge),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = colors.badgeTextColor,
        )
    }
}

@Composable
private fun BoxScope.ChangelogBannerPreviewOverlay(
    palette: CuiPalette,
    isDarkTheme: Boolean,
    haloColor: Color,
) {
    Box(modifier = Modifier.matchParentSize()) {
        ChangelogPreviewStack(
            palette = palette,
            isDarkTheme = isDarkTheme,
            haloColor = haloColor,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = BannerDimens.PreviewEndPadding)
                .width(BannerDimens.PreviewOverlayWidth)
                .offset(
                    x = BannerDimens.PreviewOffsetX,
                    y = BannerDimens.PreviewOffsetY,
                )
        )
    }
}

@Composable
private fun BoxScope.ChangelogBannerCloseButton(
    backgroundColor: Color,
    tintColor: Color,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(
                top = BannerDimens.CloseTopPadding,
                end = BannerDimens.CloseEndPadding,
            )
            .size(BannerDimens.CloseTouchTarget)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(BannerDimens.CloseContainerSize)
                .clip(CircleShape)
                .background(backgroundColor)
        ) {
            Icon(
                painter = painterResource(CommonDrawable.ic_cross),
                tint = tintColor,
                contentDescription = stringResource(R.string.main_changelog_close),
                modifier = Modifier.size(BannerDimens.CloseIconSize)
            )
        }
    }
}

@Composable
private fun BoxScope.ChangelogBannerBackdrop(
    colors: ChangelogBannerColors,
) {
    Box(modifier = Modifier.matchParentSize()) {
        BannerGlow(
            color = colors.topGlowColor,
            modifier = Modifier
                .size(BannerDimens.TopGlowSize)
                .offset(
                    x = BannerDimens.TopGlowOffsetX,
                    y = BannerDimens.TopGlowOffsetY,
                )
        )

        BannerGlow(
            color = colors.bottomGlowColor,
            modifier = Modifier
                .size(BannerDimens.BottomGlowSize)
                .align(Alignment.BottomEnd)
                .offset(
                    x = BannerDimens.BottomGlowOffsetX,
                    y = BannerDimens.BottomGlowOffsetY,
                )
        )
    }
}

@Composable
private fun BannerGlow(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(
            brush = Brush.radialGradient(
                colors = listOf(color, Color.Transparent),
            ),
            shape = CircleShape,
        )
    )
}

@Composable
private fun ChangelogPreviewStack(
    palette: CuiPalette,
    isDarkTheme: Boolean,
    haloColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        BannerGlow(
            color = haloColor,
            modifier = Modifier.size(BannerDimens.PreviewHaloSize)
        )

        ChangelogPreviewGhostCard(
            palette = palette,
            isDarkTheme = isDarkTheme,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = BannerDimens.GhostCardOffsetX,
                    y = BannerDimens.GhostCardOffsetY,
                )
                .width(BannerDimens.GhostCardWidth)
                .height(BannerDimens.GhostCardHeight)
                .graphicsLayer {
                    rotationZ = BannerDimens.GhostCardRotation
                    alpha = BannerDimens.GhostCardAlpha
                }
        )

        ChangelogPreviewCard(
            palette = palette,
            isDarkTheme = isDarkTheme,
            topAccentColor = palette.BackgroundAccentPrimary,
            bottomAccentColor = palette.BackgroundAccentSecondary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(BannerDimens.FrontCardWidth)
                .height(BannerDimens.FrontCardHeight)
                .graphicsLayer {
                    rotationZ = BannerDimens.FrontCardRotation
                }
        )
    }
}

@Composable
private fun ChangelogPreviewGhostCard(
    palette: CuiPalette,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isDarkTheme) {
        palette.BackgroundPrimary.copy(alpha = 0.18F)
    } else {
        palette.BackgroundPrimary.copy(alpha = 0.72F)
    }
    val outlineColor = palette.OutlineSecondary.copy(alpha = if (isDarkTheme) 0.32F else 0.48F)

    Box(
        modifier = modifier
            .clip(PreviewCardShape)
            .background(containerColor)
            .border(1.dp, outlineColor, PreviewCardShape)
    )
}

@Composable
private fun ChangelogPreviewCard(
    palette: CuiPalette,
    isDarkTheme: Boolean,
    topAccentColor: Color,
    bottomAccentColor: Color,
    modifier: Modifier = Modifier,
) {
    val containerColors = if (isDarkTheme) {
        listOf(
            palette.BackgroundElevationBase,
            palette.BackgroundSecondary,
        )
    } else {
        listOf(
            palette.BackgroundPrimary.copy(alpha = 0.98F),
            palette.BackgroundAccentTertiary.copy(alpha = 0.72F),
        )
    }
    val outlineColor = palette.OutlineSecondary.copy(alpha = if (isDarkTheme) 0.52F else 0.46F)
    val notchColor = palette.TextPrimary.copy(alpha = if (isDarkTheme) 0.72F else 0.8F)
    val lineColor = if (isDarkTheme) {
        palette.TextSecondary.copy(alpha = 0.18F)
    } else {
        palette.OutlineSecondary
    }
    val secondaryLineColor = lineColor.copy(alpha = if (isDarkTheme) 0.86F else 0.92F)
    val chipColor = if (isDarkTheme) {
        palette.BackgroundPrimary.copy(alpha = 0.38F)
    } else {
        palette.BackgroundPrimary.copy(alpha = 0.94F)
    }

    Box(
        modifier = modifier
            .clip(PreviewCardShape)
            .background(brush = Brush.linearGradient(containerColors))
            .border(1.dp, outlineColor, PreviewCardShape)
            .padding(
                horizontal = BannerDimens.PreviewCardHorizontalPadding,
                vertical = BannerDimens.PreviewCardVerticalPadding,
            )
    ) {
        PreviewCardNotch(notchColor = notchColor)

        PreviewCardCenterAccent(
            topAccentColor = topAccentColor,
            bottomAccentColor = bottomAccentColor,
        )

        PreviewCardBottomContent(
            lineColor = lineColor,
            secondaryLineColor = secondaryLineColor,
            topAccentColor = topAccentColor,
            bottomAccentColor = bottomAccentColor,
            chipColor = chipColor,
        )
    }
}

@Composable
private fun BoxScope.PreviewCardNotch(
    notchColor: Color,
) {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .width(BannerDimens.PreviewNotchWidth)
            .height(BannerDimens.PreviewNotchHeight)
            .clip(PillShape)
            .background(notchColor)
    )
}

@Composable
private fun BoxScope.PreviewCardCenterAccent(
    topAccentColor: Color,
    bottomAccentColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .align(Alignment.Center)
            .size(BannerDimens.PreviewCenterGlowSize)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(topAccentColor.copy(alpha = 0.32F), Color.Transparent),
                ),
                shape = CircleShape,
            )
    ) {
        Box(
            modifier = Modifier
                .size(BannerDimens.PreviewCenterDotSize)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        listOf(topAccentColor, bottomAccentColor),
                    )
                )
        )
    }
}

@Composable
private fun BoxScope.PreviewCardBottomContent(
    lineColor: Color,
    secondaryLineColor: Color,
    topAccentColor: Color,
    bottomAccentColor: Color,
    chipColor: Color,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.matchParentSize()
    ) {
        Spacer(modifier = Modifier.weight(1F))

        PreviewLine(
            widthFraction = 0.72F,
            color = lineColor,
            height = BannerDimens.PreviewLineHeight,
        )

        Spacer(modifier = Modifier.height(BannerDimens.PreviewLineSpacing))

        PreviewLine(
            widthFraction = 0.48F,
            color = secondaryLineColor,
            height = BannerDimens.PreviewSecondaryLineHeight,
        )

        Spacer(modifier = Modifier.height(BannerDimens.PreviewChipTopSpacing))

        Row(horizontalArrangement = Arrangement.spacedBy(BannerDimens.PreviewChipSpacing)) {
            PreviewChip(
                width = BannerDimens.PreviewChipPrimaryWidth,
                color = null,
                brush = Brush.linearGradient(listOf(topAccentColor, bottomAccentColor)),
            )
            PreviewChip(
                width = BannerDimens.PreviewChipSecondaryWidth,
                color = chipColor,
            )
        }
    }
}

@Composable
private fun PreviewLine(
    widthFraction: Float,
    color: Color,
    height: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .clip(PillShape)
            .background(color)
    )
}

@Composable
private fun PreviewChip(
    width: Dp,
    color: Color? = null,
    brush: Brush? = null,
) {
    val resolvedBrush = brush ?: Brush.linearGradient(listOf(color!!, color))

    Box(
        modifier = Modifier
            .width(width)
            .height(BannerDimens.PreviewChipHeight)
            .clip(PillShape)
            .background(resolvedBrush)
    )
}

@Composable
@WidgetPreview
private fun Preview() = CheckieLiteTheme {
    ChangelogBanner(
        state = WhatsNewBanner(
            title = "What's new in 1.6.0",
            subtitle = "See what changed in the latest version",
        ),
        onCloseClick = {},
        onClick = {},
        modifier = Modifier.wrapContentHeight()
    )
}
