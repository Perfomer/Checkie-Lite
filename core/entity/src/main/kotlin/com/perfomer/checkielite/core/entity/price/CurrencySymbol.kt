package com.perfomer.checkielite.core.entity.price

object CurrencySymbol {

    private val symbols: Map<String, String> = mapOf(
        "AMD" to "֏",
        "AZN" to "₼",
        "GEL" to "₾",
        "GHS" to "₵",
        "KZT" to "₸",
        "LAK" to "₭",
        "MNT" to "₮",
        "NGN" to "₦",
        "PHP" to "₱",
        "PYG" to "₲",
        "TRY" to "₺",
        "UAH" to "₴",
    )

    fun getSymbol(currencyCode: String): String? {
        return symbols[currencyCode]
    }
}