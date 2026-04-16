package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context

internal object EmptyString : Text {
    override fun resolve(context: Context): CharSequence {
        return ""
    }
}