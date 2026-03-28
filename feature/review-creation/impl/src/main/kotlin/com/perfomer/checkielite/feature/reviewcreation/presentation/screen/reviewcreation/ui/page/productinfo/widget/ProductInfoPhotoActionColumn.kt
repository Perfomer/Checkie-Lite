package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoPhotoShape

@Composable
internal fun PhotoActionColumn(
    onAddPictureClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        PhotoActionButton(
            title = stringResource(R.string.reviewcreation_productinfo_action_gallery),
            painter = painterResource(id = CommonDrawable.ic_add_picture_v2),
            onClick = onAddPictureClick,
            modifier = Modifier.weight(1f),
        )

        PhotoActionButton(
            title = stringResource(R.string.reviewcreation_productinfo_action_camera),
            painter = painterResource(id = R.drawable.ic_camera),
            onClick = onTakePhotoClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PhotoActionButton(
    title: String,
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalCuiPalette.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(ProductInfoPhotoShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.BackgroundAccentSecondary.copy(alpha = 0.2F),
                        palette.BackgroundAccentTertiary.copy(alpha = 0.95F),
                    ),
                ),
            )
            .border(
                width = 1.dp,
                color = palette.OutlineAccentSecondary.copy(alpha = 0.7F),
                shape = ProductInfoPhotoShape,
            )
            .clickable(onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(CircleShape)
                .background(palette.BackgroundPrimary.copy(alpha = 0.7F))
                .padding(10.dp),
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = palette.IconAccent,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.TextAccent,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
