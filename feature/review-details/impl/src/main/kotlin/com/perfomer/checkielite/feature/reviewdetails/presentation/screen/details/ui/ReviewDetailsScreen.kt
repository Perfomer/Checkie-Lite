package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.button.CuiIconButton
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val HORIZONTAL_PADDING_DP = 24

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
                .padding(bottom = 8.dp)
        ) {
            if (state.brandName != null) {
                Text(
                    text = state.brandName,
                    color = CuiPalette.Light.TextAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING_DP.dp),
                )

                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = state.productName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING_DP.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HORIZONTAL_PADDING_DP.dp)
            ) {
                Text(
                    text = state.date,
                    color = CuiPalette.Light.TextSecondary,
                    modifier = Modifier.weight(1F),
                )

                CheckieRating(rating = state.rating, emoji = state.emoji)
            }

            Spacer(Modifier.height(24.dp))

            PicturesCarousel(
                currentPictureIndex = state.currentPicturePosition,
                picturesUri = state.picturesUri,
            )

            if (state.reviewText != null) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = state.reviewText,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING_DP.dp)
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val pagerState = rememberPagerState(
            initialPage = currentPictureIndex,
            pageCount = { picturesUri.size },
        )

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING_DP.dp),
            pageSpacing = 12.dp,
        ) { i ->
            AsyncImage(
                model = picturesUri[i],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1F)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CuiPalette.Light.BackgroundSecondary)
            )
        }

        Spacer(Modifier.height(20.dp))

        HorizontalPagerIndicator(
            currentItem = pagerState.currentPage,
            itemsCount = pagerState.pageCount,
        )
    }
}

@Composable
private fun HorizontalPagerIndicator(
    currentItem: Int,
    itemsCount: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(itemsCount) { i -> Indicator(isSelected = currentItem == i) }
    }
}

@Composable
private fun Indicator(isSelected: Boolean) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 20.dp else 6.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "Indicator selecting",
    )

    val color = if (isSelected) {
        CuiPalette.Light.BackgroundAccentPrimary
    } else {
        CuiColorToken.GreyOrange
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(6.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppBar(
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        title = { "" },
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