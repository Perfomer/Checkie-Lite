package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagDb
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag

internal fun CheckieTagDb.toDomain(): CheckieTag {
    return CheckieTag(
        id = id,
        value = value,
        emoji = emoji,
    )
}

internal fun CheckieTag.toDb(): CheckieTagDb {
    return CheckieTagDb(
        id = id,
        value = value,
        emoji = emoji,
    )
}