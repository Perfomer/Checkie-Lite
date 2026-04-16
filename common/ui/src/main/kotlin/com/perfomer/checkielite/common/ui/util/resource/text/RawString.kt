package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context

internal class RawString(val value: CharSequence) : Text {
    override fun resolve(context: Context): CharSequence {
        return value
    }
}