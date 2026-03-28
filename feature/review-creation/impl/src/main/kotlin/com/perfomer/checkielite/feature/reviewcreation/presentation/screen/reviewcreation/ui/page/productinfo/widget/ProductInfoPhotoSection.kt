package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoContentPadding
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ProductInfoPhotoSection(
    picturesUri: ImmutableList<ProductInfoPageUiState.Picture>,
    onAddPictureClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPictureClick: (position: Int) -> Unit,
    onPictureDeleteClick: (position: Int) -> Unit,
    onPictureReorder: (pictureId: String, toPosition: Int) -> Unit,
) {
    Column {
        ProductInfoPhotoHeader(
            picturesCount = picturesUri.size,
            modifier = Modifier.padding(horizontal = ProductInfoContentPadding),
        )

        CuiSpacer(14.dp)

        ProductInfoPhotoCarousel(
            picturesUri = picturesUri,
            onAddPictureClick = onAddPictureClick,
            onTakePhotoClick = onTakePhotoClick,
            onPictureClick = onPictureClick,
            onPictureDeleteClick = onPictureDeleteClick,
            onPictureReorder = onPictureReorder,
        )
    }
}

@Composable
private fun ProductInfoPhotoHeader(
    picturesCount: Int,
    modifier: Modifier = Modifier,
) {
    val subtitleRes = if (picturesCount == 0) {
        R.string.reviewcreation_productinfo_photos_subtitle_empty
    } else {
        R.string.reviewcreation_productinfo_photos_subtitle_sort
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = stringResource(R.string.reviewcreation_productinfo_section_photos),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCuiPalette.current.TextPrimary,
            )

            Text(
                text = stringResource(subtitleRes),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = LocalCuiPalette.current.TextSecondary,
            )
        }

        if (picturesCount > 0) {
            PhotoCountBadge(count = picturesCount)
        }
    }
}
