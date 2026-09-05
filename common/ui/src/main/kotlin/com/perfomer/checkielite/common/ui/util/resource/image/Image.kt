package com.perfomer.checkielite.common.ui.util.resource.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter

@Stable
interface Image {

    @Composable
    fun resolve(): Painter

    companion object {

        fun resource(@DrawableRes resourceId: Int): Image {
            return DrawableImage(resourceId)
        }
    }
}