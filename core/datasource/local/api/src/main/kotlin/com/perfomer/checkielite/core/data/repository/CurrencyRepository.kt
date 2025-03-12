package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.domain.entity.price.CheckieCurrency

interface CurrencyRepository {

    suspend fun getAllCurrenciesCodes(): List<String>

    suspend fun getLatestCurrency(): CheckieCurrency?
}