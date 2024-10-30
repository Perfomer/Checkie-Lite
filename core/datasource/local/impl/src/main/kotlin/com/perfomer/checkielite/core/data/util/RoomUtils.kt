package com.perfomer.checkielite.core.data.util

import androidx.room.RoomDatabase

internal val RoomDatabase.version: Int
    get() = openHelper.readableDatabase.version