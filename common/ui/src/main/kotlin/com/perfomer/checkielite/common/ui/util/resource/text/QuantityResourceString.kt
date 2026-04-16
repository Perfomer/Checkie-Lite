package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context
import androidx.annotation.PluralsRes

internal data class QuantityResourceString(
    @PluralsRes val resourceId: Int,
    val quantity: Int,
    val args: List<Text>,
) : Text {

    override fun resolve(context: Context): CharSequence {
        return context.resources.getQuantityString(resourceId, quantity, *getArgValues(context, args))
    }
}