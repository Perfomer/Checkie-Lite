package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
interface Text {

    fun resolve(context: Context): CharSequence

    companion object
}

/**
 * Creates an empty [Text].
 */
fun Text.Companion.empty(): Text = EmptyString

/**
 * Creates [Text] with a hardcoded value.
 */
fun Text.Companion.raw(value: CharSequence?): Text = when {
    value.isNullOrEmpty() -> EmptyString
    else -> RawString(value)
}

fun Text.Companion.raw(value: Any?): Text = raw(value?.toString())

/**
 * Creates [Text] represented by Android resource string with optional arguments.
 */
fun Text.Companion.resource(@StringRes resourceId: Int, vararg args: Text): Text {
    return ResourceString(resourceId, args.toList())
}

/**
 * Creates [Text] represented by plural Android resource with optional arguments.
 */
fun Text.Companion.quantity(@PluralsRes resourceId: Int, quantity: Int, vararg args: Text): Text {
    return QuantityResourceString(resourceId, quantity, args.toList())
}