package com.perfomer.checkielite.common.pure.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val CHECKIE_TIME_FORMAT = "yyyy-MM-dd_HH-mm-ss"

fun getFormattedDate(date: Date = Date()): String {
    val formatter = SimpleDateFormat(CHECKIE_TIME_FORMAT, Locale.getDefault())
    return formatter.format(date)
}