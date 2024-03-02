package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.button.CuiFloatingActionButton
import com.perfomer.checkielite.common.ui.cui.button.CuiIconButton
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.ReviewItem

@Composable
internal fun MainScreen(
    state: MainUiState,
    onSearchQueryInput: (query: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
    onFabClick: () -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            CuiFloatingActionButton(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(R.string.main_add_checkie),
                onClick = onFabClick,
            )
        },
        topBar = { TopAppBar() },
    ) { contentPadding ->
        when (state) {
            is MainUiState.Loading -> Unit

            is MainUiState.Content -> Content(
                state = state,
                contentPadding = contentPadding,
                onSearchQueryInput = onSearchQueryInput,
                onSearchQueryClearClick = onSearchQueryClearClick,
                onReviewClick = onReviewClick,
            )

            is MainUiState.Empty -> Empty()
        }
    }
}

@Composable
private fun Content(
    state: MainUiState.Content,
    contentPadding: PaddingValues,
    onSearchQueryInput: (query: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
    onReviewClick: (id: String) -> Unit = {},
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            SearchField(
                searchQuery = state.searchQuery,
                onSearchQueryInput = onSearchQueryInput,
                onSearchQueryClearClick = onSearchQueryClearClick,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(
            count = state.reviews.size,
            key = { i -> state.reviews[i].id },
        ) { i ->
            CheckieHorizontalItem(state.reviews[i], onReviewClick)
        }
    }
}

@Composable
private fun Empty() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(CommonDrawable.ill_empty),
            contentDescription = null,
            modifier = Modifier.width(184.dp),
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.main_empty_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.main_empty_description),
            fontSize = 14.sp,
            color = CuiPalette.Light.TextSecondary,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    append(
                        AnnotatedString(
                            text = stringResource(id = R.string.app_name_checkie),
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        )
                    )
                    append(" ")
                    append(
                        AnnotatedString(
                            text = stringResource(id = R.string.app_name_lite),
                            spanStyle = SpanStyle(fontWeight = FontWeight.Normal)
                        )
                    )
                },
                fontSize = 20.sp,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(CuiPalette.Light.BackgroundPrimary)
            .statusBarsPadding()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (query: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryInput,
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(R.string.main_clear),
                    onClick = onSearchQueryClearClick,
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = CuiPalette.Light.OutlineSecondary,
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences
        ),
        placeholder = { Text(stringResource(R.string.main_search)) },
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
                .background(CuiPalette.Light.BackgroundSecondary)
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
                    tint = CuiPalette.Light.IconSecondary,
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
            )

            if (item.brand != null) {
                Text(
                    text = item.brand.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = CuiPalette.Light.TextAccent,
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
            fontSize = 14.sp,
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
private fun MainScreenPreview() = PreviewTheme {
    MainScreen(state = mockUiState)
}

internal val mockUiState = MainUiState.Content(
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