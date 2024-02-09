package com.perfomer.checkielite.core.data.datasource.room.converter

import androidx.room.TypeConverter
import java.util.Date

internal class DateConverter {

    @TypeConverter
    fun to(item: Date) = item.time

    @TypeConverter
    fun from(source: Long) = Date(source)

}