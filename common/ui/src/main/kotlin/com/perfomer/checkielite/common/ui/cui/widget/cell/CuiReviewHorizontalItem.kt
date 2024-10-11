package com.perfomer.checkielite.common.ui.cui.widget.cell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.cui.widget.rating.ReviewRating
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiFadedText
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Immutable
data class ReviewItem(
    val id: String,
    val title: String,
    val brand: String?,
    val imageUri: String?,
    val rating: Int,
    val isSyncing: Boolean,
)

@Composable
fun CuiReviewHorizontalItem(
    item: ReviewItem,
    onClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(item.id) }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LocalCuiPalette.current.BackgroundSecondary)
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
                    tint = LocalCuiPalette.current.IconTertiary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            SyncingBlock(item.isSyncing)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1F)) {
            CuiFadedText(
                text = item.title,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            if (item.brand != null) {
                CuiFadedText(
                    text = item.brand.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = LocalCuiPalette.current.TextAccent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        ReviewRating(rating = item.rating)
    }
}

@Composable
private fun SyncingBlock(isSyncing: Boolean) {
    AnimatedVisibility(visible = isSyncing, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .background(LocalCuiPalette.current.BackgroundPrimary.copy(alpha = 0.5F))
                .padding(12.dp)
        ) {
            CircularProgressIndicator(
                color = LocalCuiPalette.current.IconPrimary,
                strokeWidth = 2.dp,
            )
        }
    }
}