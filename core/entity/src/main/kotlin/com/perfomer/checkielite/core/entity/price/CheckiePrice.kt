package com.perfomer.checkielite.core.entity.price

import java.math.BigDecimal

data class CheckiePrice(
    val currency: CheckieCurrency,
    val value: BigDecimal,
)