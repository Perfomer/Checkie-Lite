package group.bakemate.common.ui.util

import android.util.Patterns

fun String.isNotEmail(): Boolean {
    return !Patterns.EMAIL_ADDRESS.matcher(this).matches()
}