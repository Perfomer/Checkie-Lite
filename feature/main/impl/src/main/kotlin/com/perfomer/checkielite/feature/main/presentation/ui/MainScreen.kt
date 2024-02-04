package com.perfomer.checkielite.feature.main.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.perfomer.checkielite.feature.main.impl.R
import com.perfomer.checkielite.feature.main.presentation.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.ui.state.ReviewItem
import com.perfomer.checkielite.common.ui.theme.ScreenPreview

@Composable
internal fun MainScreen(
    state: MainUiState,
    onSearchQueryInput: (query: String) -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onFabClick: () -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFFF17A54),
                contentColor = Color.White,
                shape = CircleShape,
                onClick = onFabClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                )
            }
        },
        topBar = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color.White,
                                Color.White,
                                Color.White.copy(alpha = 0F)
                            )
                        ),
                    )
                    .statusBarsPadding()
            ) {
                Text(
                    text = "checkie lite",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color(0xFF222B39),
                )
            }
        },
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SearchField(state.searchQuery, onSearchQueryInput)
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(state.reviews.size) { i ->
                CheckieHorizontalItem(state.reviews[i], onReviewClick)
            }
        }
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (query: String) -> Unit,
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryInput,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE6E6E6),
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        placeholder = { Text("Search") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Composable
internal fun CheckieHorizontalItem(
    item: ReviewItem,
    onClick: (id: String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item.id) }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFEDEDED))
        ) {
            if (item.imageUri != null) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_image),
                    tint = Color(0xFFC1C0CC),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF222B39),
            )

            if (item.brand != null) {
                Text(
                    text = item.brand.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFFDB4819),
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        CheckieRating(rating = item.rating, emoji = item.emoji)
    }
}

@Composable
private fun CheckieRating(rating: Int, emoji: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = rating.toString(),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF222B39),
        )

        Text(
            text = "/10",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF656160),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = emoji, fontSize = 24.sp)
    }
}

@ScreenPreview
@Composable
private fun MainScreenPreview() {
    MainScreen(state = mockUiState)
}

internal val mockUiState = MainUiState(
    searchQuery = "",
    reviews = listOf(
        ReviewItem(
            id = "1",
            title = "Chicken toasts with poached eggs",
            brand = "Lui Bidon",
            imageUri = "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
            rating = 8,
            emoji = "\uD83D\uDE0D", // 😍
        ),
        ReviewItem(
            id = "2",
            title = "Lemonblast",
            brand = "Darkside",
            imageUri = "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
            rating = 0,
            emoji = "\uD83D\uDCA9", // 💩
        ),
        ReviewItem(
            id = "3",
            title = "One Flew Over the Cuckoos Next",
            brand = "Key Kesey",
            imageUri = "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
            rating = 10,
            emoji = "\uD83D\uDC8E", // 💎
        ),
        ReviewItem(
            id = "4",
            title = "The Wolf of Wall Street",
            brand = null,
            imageUri = "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80",
            rating = 4,
            emoji = "\uD83D\uDE10", // 😐
        ),
        ReviewItem(
            id = "5",
            title = "My own dog",
            brand = null,
            imageUri = null,
            rating = 3,
            emoji = "\uD83D\uDE2D", // 😭
        ),
    )
)