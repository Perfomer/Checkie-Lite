package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

val Text?.isEmpty: Boolean
    get() = this == null || this is EmptyString

operator fun Text.plus(other: Text): Text {
    return CompoundString(this, other)
}

operator fun Text.plus(other: CharSequence): Text {
    return this + Text.raw(other)
}

internal fun getArgValues(context: Context, args: List<Text>): Array<CharSequence> {
    return args.map { text -> text.resolve(context) }
        .toTypedArray()
}

@Composable
@ReadOnlyComposable
fun text(text: Text): String {
    return text.resolve(LocalContext.current).toString()
}

@Composable
@ReadOnlyComposable
fun textOrNull(text: Text?): String? {
    return text?.let { text(it) }
}