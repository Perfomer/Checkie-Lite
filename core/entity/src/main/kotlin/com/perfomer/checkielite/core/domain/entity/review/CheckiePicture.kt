package com.perfomer.checkielite.core.domain.entity.review

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