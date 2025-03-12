package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckiePriceDb
import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.domain.entity.price.CheckiePrice

internal fun CheckiePrice.toDb(): CheckiePriceDb {
    return CheckiePriceDb(value = value, currencyCode = currency.code)
}

internal fun CheckiePriceDb.toDomain(): CheckiePrice {
    return CheckiePrice(value = value, currency = CheckieCurrency(currencyCode))
}