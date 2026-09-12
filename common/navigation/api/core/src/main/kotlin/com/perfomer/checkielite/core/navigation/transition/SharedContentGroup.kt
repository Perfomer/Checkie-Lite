package com.perfomer.checkielite.core.navigation.transition

/** Application-defined content category allowed by a navigation edge. Has no UI dependencies. */
interface SharedContentGroup {

    /** Compatibility group for content without an application-specific category. */
    data object Default : SharedContentGroup
}

