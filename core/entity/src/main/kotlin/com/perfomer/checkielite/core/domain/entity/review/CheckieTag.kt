package com.perfomer.checkielite.core.domain.entity.review

import java.io.Serializable

data class CheckieTag(
    val id: String,
    val value: String,
    val emoji: String?,
) : Serializable