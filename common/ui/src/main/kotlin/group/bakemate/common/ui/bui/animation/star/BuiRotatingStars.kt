package group.bakemate.common.ui.bui.animation.star

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import group.bakemate.common.ui.R
import group.bakemate.common.ui.bui.animation.rotation.BuiInternalRotatingIcon
import group.bakemate.common.ui.bui.animation.rotation.RotationDirection
import group.bakemate.common.ui.theme.OrangeLight
import group.bakemate.common.ui.theme.White
import group.bakemate.common.ui.theme.WidgetPreview

@Composable
fun BuiRotatingStars(tintColor: Color, scrimColor: Color) {
    val scrim = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            scrimColor,
        ),
        tileMode = TileMode.Mirror
    )

    Box(
        contentAlignment = Alignment.Center,
    ) {
        BuiInternalRotatingIcon(
            resource = R.drawable.ic_star_first,
            tintColor = tintColor,
            rotationDirection = RotationDirection.LEFT,
            animationDuration = 100000,
            label = "first star",
        )
        BuiInternalRotatingIcon(
            resource = R.drawable.ic_star_second,
            tintColor = tintColor,
            rotationDirection = RotationDirection.RIGHT,
            animationDuration = 50000,
            label = "second star",
        )
        BuiInternalRotatingIcon(
            resource = R.drawable.ic_star_third,
            tintColor = tintColor,
            rotationDirection = RotationDirection.LEFT,
            animationDuration = 30000,
            label = "third star",
        )
        BuiInternalRotatingIcon(
            resource = R.drawable.ic_star_fourth,
            tintColor = tintColor,
            rotationDirection = RotationDirection.RIGHT,
            animationDuration = 10000,
            label = "fourth star",
        )
        Box(modifier = Modifier
            .matchParentSize()
            .background(scrim)
        )
    }
}

@WidgetPreview
@Composable
private fun BuiRotatingStarsPreview() {
    BuiRotatingStars(tintColor = OrangeLight, scrimColor = White)
}
