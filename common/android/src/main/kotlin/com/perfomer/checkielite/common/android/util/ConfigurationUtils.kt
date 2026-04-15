package com.perfomer.checkielite.common.android.util

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.util.Locale

val Context.currentLocale: Locale
    get() = ConfigurationCompat.getLocales(resources.configuration)[0] ?: Locale.getDefault()