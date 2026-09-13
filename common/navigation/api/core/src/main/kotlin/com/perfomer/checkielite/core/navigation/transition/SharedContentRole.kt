package com.perfomer.checkielite.core.navigation.transition

/** Describes how content is presented, without knowing any destination. */
interface SharedContentRole {
    val group: SharedContentGroup

    data class Default(override val group: SharedContentGroup) : SharedContentRole
}
