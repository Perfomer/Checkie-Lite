package com.perfomer.checkielite.core.entity

data class CheckiePicture(
    val id: String = NO_ID,
    val uri: String,
) {
    companion object {
        const val NO_ID: String = "no_picture_id"
    }
}