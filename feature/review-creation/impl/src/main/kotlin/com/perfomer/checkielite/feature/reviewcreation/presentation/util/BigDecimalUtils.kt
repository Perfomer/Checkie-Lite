package com.perfomer.checkielite.feature.reviewcreation.presentation.util

import java.math.BigDecimal

internal fun String.toBigDecimalSafe(): BigDecimal? {
    return if (this.isNotEmpty()) BigDecimal(this.dropLastWhile { it == '.' }) else null
}