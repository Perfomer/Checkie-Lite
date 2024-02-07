package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState

@Composable
internal fun ReviewDetailsScreen(
    state: ReviewDetailsUiState,
) {
    Scaffold(
        topBar = { TopAppBar() },
    ) { contentPadding ->

    }
}

@Composable
private fun TopAppBar() {

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

//@ScreenPreview
//@Composable
//private fun ReviewDetailsScreenPreview() {
//    ReviewDetailsScreen(state = mockUiState)
//}
//
//internal val mockUiState = ReviewDetailsUiState.Content(
//
//            title = "Chicken toasts with poached eggs",
//            brand = "Lui Bidon",
//            picturesUri = listOf(
//                "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
//                "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
//                "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
//                "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"
//            ),
//            rating = 8,
//            emoji = "\uD83D\uDE0D", // 😍
//        ),
//    )
//)