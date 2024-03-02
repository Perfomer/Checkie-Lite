package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
internal fun EmptyImage(onEmptyImageClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .aspectRatio(1F)
            .clip(RoundedCornerShape(24.dp))
            .background(CuiPalette.Light.BackgroundSecondary)
            .clickable(onClick = onEmptyImageClick)
    ) {
        Icon(
            painter = painterResource(id = CommonDrawable.ic_add_picture),
            tint = CuiPalette.Light.IconAccent,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.reviewdetails_add_image),
            color = CuiPalette.Light.TextAccent,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}