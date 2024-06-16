package com.perfomer.checkielite.core.entity.price

import java.io.Serializable
import java.util.Currency

data class CheckieCurrency(
    val code: String,
    val symbol: String,
) : Serializable {

    constructor(code: String) : this(
        code = code,
        symbol = Currency.getInstance(code).symbol,
    )
}