package com.perfomer.checkielite.core.entity

import java.io.Serializable

data class CheckiePicture(
    val id: String,
    val uri: String,
    val source: PictureSource,
) : Serializable

enum class PictureSource {
    GALLERY,
    APP,
}