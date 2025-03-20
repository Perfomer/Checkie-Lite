package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiFadedText
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
internal fun ReviewCreationPageHeader(
    title: String,
    productPictureUri: String?,
    productName: String,
    endIcon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1F)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HeaderReviewPicture(
                    pictureUri = productPictureUri,
                )

                CuiFadedText(
                    text = productName,
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        endIcon?.invoke()
    }
}

@Composable
private fun HeaderReviewPicture(pictureUri: String?) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(LocalCuiPalette.current.BackgroundSecondary)
    ) {
        if (pictureUri != null) {
            AsyncImage(
                model = pictureUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = LocalCuiPalette.current.OutlinePicture,
                    )
            )
        } else {
            Icon(
                painter = painterResource(id = com.perfomer.checkielite.common.ui.R.drawable.ic_image),
                tint = LocalCuiPalette.current.IconTertiary,
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}