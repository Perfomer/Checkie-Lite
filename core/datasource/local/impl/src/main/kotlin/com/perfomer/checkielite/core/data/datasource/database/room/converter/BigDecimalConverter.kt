package com.perfomer.checkielite.core.data.datasource.database.room.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

internal class BigDecimalConverter {

    @TypeConverter
    fun to(item: BigDecimal) = item.toString()

    @TypeConverter
    fun from(source: String) = BigDecimal(source)

}