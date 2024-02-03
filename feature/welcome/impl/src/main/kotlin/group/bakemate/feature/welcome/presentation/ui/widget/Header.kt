package group.bakemate.feature.welcome.presentation.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import group.bakemate.common.ui.CommonString
import group.bakemate.common.ui.bui.span.buiAppNameAnnotated
import group.bakemate.common.ui.bui.animation.star.BuiRotatingStars
import group.bakemate.common.ui.theme.Brown
import group.bakemate.common.ui.theme.OrangeDark
import group.bakemate.common.ui.theme.OrangeLight
import group.bakemate.common.ui.theme.White
import group.bakemate.feature.welcome.impl.R

@Composable
internal fun Header() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-16).dp)
    ) {
        BuiRotatingStars(
            tintColor = OrangeLight,
            scrimColor = White,
        )

        Image(
            painter = painterResource(id = R.drawable.ic_slice),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(96.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(32),
                    spotColor = OrangeDark,
                )
        )

        HeaderText(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun HeaderText(modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = buiAppNameAnnotated(),
            fontSize = 32.sp,
        )
        Text(
            text = stringResource(id = CommonString.app_description).uppercase(),
            fontSize = 12.sp,
            color = Brown
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}