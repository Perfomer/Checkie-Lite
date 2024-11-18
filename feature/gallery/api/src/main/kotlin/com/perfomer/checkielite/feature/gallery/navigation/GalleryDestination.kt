package com.perfomer.checkielite.feature.gallery.navigation

import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class GalleryDestination(
    val picturesUri: @Contextual List<String>,
    val currentPicturePosition: Int,
) : Destination()
