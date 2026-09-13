package com.perfomer.checkielite.core.navigation.transition

/** Application-defined content category allowed by a navigation edge. Has no UI dependencies. */
interface SharedContentGroup {

    /** Declare each role in a val: separate calls create distinct identities within this group. */
    fun role(): SharedContentRole = OwnedRole(this)

    /** Compatibility group for content without an application-specific category. */
    data object Default : SharedContentGroup
}

// Roles in the same group must remain distinct: equality is by identity, not by group.
private class OwnedRole(override val group: SharedContentGroup) : SharedContentRole
