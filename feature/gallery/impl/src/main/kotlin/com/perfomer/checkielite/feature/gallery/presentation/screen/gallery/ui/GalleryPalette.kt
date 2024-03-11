package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal object GalleryPalette {

    val BackgroundColor = Color.Black
    val BarColor = Color.Black.copy(alpha = 0.65F)
    val ContentColor = Color.White

    val BottomScrim = Brush.verticalGradient(
        0F to Color.Transparent,
        0.75F to BarColor,
        1F to BarColor,
    )

    val TopScrim = Brush.verticalGradient(
        0F to BarColor,
        0.55F to BarColor,
        1F to Color.Transparent,
    )
}