package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.pager.CuiHorizontalPagerIndicator
import com.perfomer.checkielite.common.ui.cui.pager.offsetForPage
import com.perfomer.checkielite.common.ui.cui.pager.scaleHorizontalNeighbors
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.absoluteValue

private const val HORIZONTAL_PADDING = 24

@Composable
internal fun ReviewDetailsScreen(
    state: ReviewDetailsUiState,
    onNavigationIconClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { AppBar(onNavigationIconClick) },
    ) { contentPadding ->
        if (state !is ReviewDetailsUiState.Content) return@Scaffold

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(bottom = 16.dp)
        ) {
            if (state.brandName != null) {
                Text(
                    text = state.brandName,
                    color = CuiPalette.Light.TextAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING.dp),
                )

                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = state.productName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HORIZONTAL_PADDING.dp)
            ) {
                Text(
                    text = state.date,
                    color = CuiPalette.Light.TextSecondary,
                    modifier = Modifier.weight(1F),
                )

                CheckieRating(rating = state.rating, emoji = state.emoji)
            }

            PicturesCarousel(
                currentPictureIndex = state.currentPicturePosition,
                picturesUri = state.picturesUri,
            )

            if (state.reviewText != null) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = state.reviewText,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING.dp)
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PicturesCarousel(
    currentPictureIndex: Int,
    picturesUri: ImmutableList<String>,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState(
            initialPage = currentPictureIndex,
            pageCount = { picturesUri.size },
        )

        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            contentPadding = PaddingValues(
                horizontal = HORIZONTAL_PADDING.dp,
                vertical = 24.dp
            ),
        ) { i ->
            Box(
                modifier = Modifier.scaleHorizontalNeighbors(pagerState = pagerState, page = i)
            ) {
                var pictureState: AsyncImagePainter.State by remember(i) { mutableStateOf(AsyncImagePainter.State.Empty) }

                if (pictureState is AsyncImagePainter.State.Success) {
                    Image(
                        painter = requireNotNull(pictureState.painter),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1F)
                            .blur(40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .alpha((1F - pagerState.offsetForPage(i).absoluteValue) * 0.8F)
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

        CuiHorizontalPagerIndicator(
            state = pagerState,
            selectedWidth = 20.dp,
            defaultWidth = 6.dp,
            selectedColor = CuiPalette.Light.BackgroundAccentPrimary,
            defaultColor = CuiColorToken.GreyOrange,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppBar(
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            CuiIconButton(
                painter = painterResource(CommonDrawable.ic_arrow_back),
                onClick = onNavigationIconClick,
            )
        },
    )
}

@Composable
private fun CheckieRating(rating: Int, emoji: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = rating.toString(),
            fontWeight = FontWeight.Medium,
        )

        Text(
            text = "/10",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = CuiPalette.Light.TextSecondary,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = emoji, fontSize = 24.sp)
    }
}

@ScreenPreview
@Composable
private fun ReviewDetailsScreenPreview() {
    ReviewDetailsScreen(state = mockUiState)
}

internal val mockUiState = ReviewDetailsUiState.Content(
    productName = "Chicken toasts with poached eggs",
    brandName = "LUI BIDON",
    picturesUri = persistentListOf(
        "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
        "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
        "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
        "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"
    ),
    rating = 8,
    emoji = "\uD83D\uDE0D", // 😍
    date = "31 May 2023",
    currentPicturePosition = 0,
    reviewText = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
)