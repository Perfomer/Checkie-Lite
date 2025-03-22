package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.offsetForPage
import com.perfomer.checkielite.common.ui.cui.modifier.scaleHorizontalNeighbors
import com.perfomer.checkielite.common.ui.cui.widget.pager.CuiHorizontalPagerIndicator
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.absoluteValue

@Composable
internal fun PicturesCarousel(
    currentPictureIndex: Int,
    picturesUri: ImmutableList<String>,
    onPageChange: (pageIndex: Int) -> Unit,
    onPictureClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState(
            initialPage = currentPictureIndex,
            pageCount = { picturesUri.size },
        )

        UpdateEffect(pagerState.currentPage) { onPageChange(pagerState.currentPage) }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 24.dp
            ),
        ) { i ->
            Box(
                modifier = Modifier.scaleHorizontalNeighbors(pagerState = pagerState, page = i)
            ) {
                var pictureState: AsyncImagePainter.State by remember(i) { mutableStateOf(AsyncImagePainter.State.Empty) }

                if (pictureState is AsyncImagePainter.State.Success) {
                    val isSystemInDarkTheme = isSystemInDarkTheme()
                    val themeCoefficient = if (isSystemInDarkTheme) 0.5F else 0.9F
                    val targetAlpha = (1F - pagerState.offsetForPage(i).absoluteValue) * themeCoefficient
                    val interpolatedAlpha = FastOutLinearInEasing.transform(targetAlpha)
                    Image(
                        painter = requireNotNull(pictureState.painter),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1F)
                            .blur(40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .graphicsLayer {
                                alpha = interpolatedAlpha
                                translationY = 40.dp.toPx()
                            }
                            .clip(RoundedCornerShape(24.dp))
                    )
                }

                AsyncImage(
                    model = picturesUri[i],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onState = { state -> pictureState = state },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1F)
                        .clip(RoundedCornerShape(24.dp))
                        .background(LocalCuiPalette.current.BackgroundSecondary)
                        .clickable(onClick = onPictureClick)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (pagerState.pageCount > 1) {
            CuiHorizontalPagerIndicator(
                state = pagerState,
                selectedWidth = 20.dp,
                defaultWidth = 6.dp,
                selectedColor = LocalCuiPalette.current.BackgroundAccentPrimary,
                defaultColor = LocalCuiPalette.current.BackgroundTertiary,
            )
        }
    }
}