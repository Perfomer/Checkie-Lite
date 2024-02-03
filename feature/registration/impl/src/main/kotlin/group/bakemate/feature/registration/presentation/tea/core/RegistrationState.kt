package group.bakemate.feature.registration.presentation.tea.core

import group.bakemate.feature.registration.presentation.entity.Condition
import group.bakemate.feature.registration.presentation.entity.FieldType

internal data class RegistrationState(
    val email: String = "",
    val nickname: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val conditions: Map<FieldType, Condition> = mapOf(),
    val isSending: Boolean = false
)