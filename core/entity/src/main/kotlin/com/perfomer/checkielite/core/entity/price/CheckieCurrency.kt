package com.perfomer.checkielite.core.entity.price

import kotlinx.serialization.Serializable
import java.util.Currency

@Serializable
data class CheckieCurrency(
    val code: String,
    val symbol: String,
) {

    constructor(code: String) : this(
        code = code,
        symbol = Currency.getInstance(code).symbol,
    )
}