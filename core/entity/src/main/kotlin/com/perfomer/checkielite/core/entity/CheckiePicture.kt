package com.perfomer.checkielite.core.entity

import java.io.Serializable

data class CheckiePicture(
    val id: String = NO_ID,
    val uri: String,
    val source: PictureSource,
) : Serializable {
    companion object {
        const val NO_ID: String = "no_picture_id"
    }
}

enum class PictureSource {
    GALLERY,
    APP,
}