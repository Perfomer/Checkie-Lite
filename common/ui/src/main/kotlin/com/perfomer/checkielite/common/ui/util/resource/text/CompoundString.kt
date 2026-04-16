package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context

internal data class CompoundString(
    val parts: List<Text>
) : Text {

    constructor(vararg parts: Text) : this(parts.toList())

    override fun resolve(context: Context): CharSequence {
        return parts.joinToString(separator = "") { text -> text.resolve(context) }
    }
}