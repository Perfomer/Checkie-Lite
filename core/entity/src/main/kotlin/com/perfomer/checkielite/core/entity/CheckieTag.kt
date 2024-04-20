package com.perfomer.checkielite.core.entity

import java.io.Serializable

data class CheckieTag(
    val id: String,
    val value: String,
    val emoji: String?,
) : Serializable