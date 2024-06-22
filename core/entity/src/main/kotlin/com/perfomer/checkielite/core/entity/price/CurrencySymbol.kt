package com.perfomer.checkielite.core.entity.price

object CurrencySymbol {

    private val symbols: Map<String, String> = mapOf(
        "ARS" to "$",
        "AUD" to "$",
        "AZN" to "₼",
        "BND" to "$",
        "CAD" to "$",
        "CLP" to "$",
        "COP" to "$",
        "CUP" to "$",
        "DOP" to "$",
        "GEL" to "₾",
        "GHS" to "₵",
        "KZT" to "₸",
        "LAK" to "₭",
        "MNT" to "₮",
        "MXN" to "$",
        "NGN" to "₦",
        "PHP" to "₱",
        "PYG" to "₲",
        "TRY" to "₺",
        "UAH" to "₴",
        "UYU" to "$",
    )

    fun getSymbol(currencyCode: String): String? {
        return symbols[currencyCode]
    }
}