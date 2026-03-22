@file:SuppressLint("ModifierFactoryReturnType", "ModifierFactoryExtensionFunction")

package com.perfomer.checkielite.common.ui.cui.modifier

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier): ConditionalModifier {
    return if (condition) {
        ConditionalModifier(then(modifier()), true)
    } else {
        ConditionalModifier(this, false)
    }
}

inline fun <T : Any> Modifier.thenIfNotNull(value: T?, modifier: Modifier.(T) -> Modifier): ConditionalModifier {
    return thenIf(value != null) { modifier(value!!) }
}

inline fun ConditionalModifier.thenElse(modifier: Modifier.() -> Modifier): Modifier {
    return if (!condition) {
        modifier()
    } else {
        this
    }
}

class ConditionalModifier @PublishedApi internal constructor(
    source: Modifier,
    @PublishedApi internal val condition: Boolean,
) : Modifier by source