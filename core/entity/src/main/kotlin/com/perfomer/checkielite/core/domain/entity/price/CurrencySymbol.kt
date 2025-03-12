package com.perfomer.checkielite.core.domain.entity.price

object CurrencySymbol {

    private val symbols: Map<String, String> = mapOf(
        "AMD" to "֏",
        "AWG" to "ƒ",
        "AZN" to "₼",
        "CNY" to "¥",
        "CRC" to "₡",
        "GEL" to "₾",
        "GHS" to "₵",
        "KHR" to "៛",
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