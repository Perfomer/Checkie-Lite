package com.perfomer.checkielite.core.navigation.transition

/** One directional channel. Both endpoints use this same value in their shared keys. */
data class SharedContentMatch(
    val source: SharedContentRole,
    val target: SharedContentRole,
) {
    init {
        require(source.group == target.group) { "Matched roles must belong to the same content group" }
    }

    val group: SharedContentGroup get() = source.group
}

class SharedTransitionPolicy(matches: Set<SharedContentMatch>) {
    val matches: Set<SharedContentMatch> = matches.toSet()
    val groups: Set<SharedContentGroup> = matches.map { it.group }.toSet()

    init {
        require(matches.map { it.source }.distinct().size == matches.size) {
            "A source role must have exactly one target role"
        }
        require(matches.map { it.target }.distinct().size == matches.size) {
            "A target role must have exactly one source role"
        }
    }

    fun match(role: SharedContentRole, isSource: Boolean): SharedContentMatch? =
        matches.singleOrNull { (if (isSource) it.source else it.target) == role }

    companion object {
        fun forGroups(groups: Set<SharedContentGroup>): SharedTransitionPolicy = SharedTransitionPolicy(
            groups.map { group ->
                val role = SharedContentRole.Default(group)
                SharedContentMatch(role, role)
            }.toSet(),
        )
    }
}
