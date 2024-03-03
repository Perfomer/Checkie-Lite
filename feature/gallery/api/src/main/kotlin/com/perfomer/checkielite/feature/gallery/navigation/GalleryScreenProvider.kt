package com.perfomer.checkielite.feature.gallery.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.core.navigation.api.Params

data class GalleryParams(
    val picturesUri: ArrayList<String>,
    val currentPicturePosition: Int,
) : Params

fun interface GalleryScreenProvider {

    operator fun invoke(params: GalleryParams): CheckieScreen
}