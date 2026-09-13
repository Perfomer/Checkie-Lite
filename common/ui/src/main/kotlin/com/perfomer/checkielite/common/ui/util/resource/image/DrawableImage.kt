package com.perfomer.checkielite.common.ui.util.resource.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

internal data class DrawableImage(
    @DrawableRes val resourceId: Int,
) : Image {

    @Composable
    override fun resolve(): Painter {
        return painterResource(resourceId)
    }
}