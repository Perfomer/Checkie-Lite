package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.pager.CuiHorizontalPagerIndicator
import com.perfomer.checkielite.common.ui.cui.pager.offsetForPage
import com.perfomer.checkielite.common.ui.cui.pager.scaleHorizontalNeighbors
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.absoluteValue

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun PicturesCarousel(
    currentPictureIndex: Int,
    picturesUri: ImmutableList<String>,
    onPageChange: (pageIndex: Int) -> Unit,
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
                    val alpha = (1F - pagerState.offsetForPage(i).absoluteValue) * 0.75F
                    val interpolatedAlpha = FastOutLinearInEasing.transform(alpha)
                    Image(
                        painter = requireNotNull(pictureState.painter),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1F)
                            .blur(40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .alpha(interpolatedAlpha)
                            .clip(RoundedCornerShape(24.dp))
                            .background(CuiPalette.Light.BackgroundSecondary)
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
                        .background(CuiPalette.Light.BackgroundSecondary)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (pagerState.pageCount > 1) {
            CuiHorizontalPagerIndicator(
                state = pagerState,
                selectedWidth = 20.dp,
                defaultWidth = 6.dp,
                selectedColor = CuiPalette.Light.BackgroundAccentPrimary,
                defaultColor = CuiColorToken.GreyOrange,
            )
        }
    }
}