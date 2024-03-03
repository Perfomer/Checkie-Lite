package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.productinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProductInfoScreen(
    state: ProductInfoPageUiState,
    onProductNameTextInput: (String) -> Unit = {},
    onBrandTextInput: (String) -> Unit = {},
    onAddPictureClick: () -> Unit = {},
    onPictureClick: (String) -> Unit = {},
    onPictureDeleteClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .imePadding()
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_productinfo_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(16.dp))

        CuiOutlinedField(
            text = state.productName,
            errorText = state.productNameErrorText,
            title = stringResource(R.string.reviewcreation_productinfo_field_product),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            onValueChange = onProductNameTextInput,
        )

        Spacer(Modifier.height(4.dp))

        CuiOutlinedField(
            text = state.brand,
            title = stringResource(R.string.reviewcreation_productinfo_field_brand),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            onValueChange = onBrandTextInput,
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                AddPicture(
                    onClick = onAddPictureClick,
                    modifier = Modifier.padding(top = 6.dp, end = 6.dp)
                )
            }

            state.picturesUri.forEach { pictureUri ->
                key(pictureUri) {
                    DeletablePicture(
                        pictureUrl = pictureUri,
                        onClick = { onPictureClick(pictureUri) },
                        onDeletePictureClick = { onPictureDeleteClick(pictureUri) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun AddPicture(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = CommonDrawable.ic_add_picture),
        contentDescription = null,
        tint = CuiColorToken.OrangeDark,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CuiPalette.Light.BackgroundAccentSecondary)
            .clickable(onClick = onClick)
            .padding(16.dp),
    )
}


@Composable
private fun DeletablePicture(
    pictureUrl: String,
    onClick: () -> Unit,
    onDeletePictureClick: () -> Unit,
) {
    Box(contentAlignment = Alignment.TopEnd) {
        Picture(
            pictureUrl = pictureUrl,
            onClick = onClick,
            modifier = Modifier.padding(top = 6.dp, end = 6.dp)
        )

        DeleteIconButton(onClick = onDeletePictureClick)
    }
}

@Composable
private fun DeleteIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(CommonDrawable.ic_cross),
        tint = CuiPalette.Light.IconPrimary,
        contentDescription = null,
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(CuiPalette.Light.BackgroundSecondary)
            .clickable(onClick = onClick)
            .padding(3.dp)
    )
}

@Composable
internal fun Picture(
    pictureUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = pictureUrl,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CuiPalette.Light.BackgroundSecondary)
            .clickable(onClick = onClick)
    )
}

@Composable
@ScreenPreview
private fun ProductInfoScreenPreview() {
    ProductInfoScreen(
        state = ProductInfoPageUiState(
            productName = "Aboba",
            productNameErrorText = null,
            brand = "Abobov",
            picturesUri = persistentListOf(
                "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
                "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
                "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"
            )
        )
    )
}