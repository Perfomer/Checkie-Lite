package group.bakemate.common.ui.bui.animation.rotation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun BuiInternalRotatingIcon(
    @DrawableRes resource: Int,
    tintColor: Color,
    rotationDirection: RotationDirection,
    animationDuration: Int,
    label: String,
) {
    val infiniteTransition = rememberInfiniteTransition(label = label)

    val leftRotationAngle by infiniteTransition.animateFloat(
        initialValue = 360F,
        targetValue = 0F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing)
        ),
        label = label
    )

    val rightRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing)
        ),
        label = label
    )

    val angle = when(rotationDirection) {
        RotationDirection.LEFT -> leftRotationAngle
        RotationDirection.RIGHT -> rightRotationAngle
    }

    Icon(
        painter = painterResource(id = resource),
        contentDescription = null,
        tint = tintColor,
        modifier = Modifier.rotate(angle),
    )
}