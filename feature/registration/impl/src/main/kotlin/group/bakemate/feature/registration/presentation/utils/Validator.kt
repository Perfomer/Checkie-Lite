package group.bakemate.feature.registration.presentation.utils

import group.bakemate.common.ui.util.isNotEmail
import group.bakemate.feature.registration.presentation.entity.Condition
import group.bakemate.feature.registration.presentation.entity.ConfirmPasswordCondition
import group.bakemate.feature.registration.presentation.entity.EmailCondition
import group.bakemate.feature.registration.presentation.entity.FieldType
import group.bakemate.feature.registration.presentation.entity.FieldType.*
import group.bakemate.feature.registration.presentation.entity.NicknameCondition
import group.bakemate.feature.registration.presentation.entity.PasswordCondition


private const val NICKNAME_MIN_LENGTH = 2
private const val PASSWORD_MIN_LENGTH = 6
internal fun validate(fieldType: FieldType, text: Any, comparedText: Any = "") = when(fieldType) {
    EMAIL -> validateEmail(text as String)
    NICKNAME -> validateNickname(text as String)
    PASSWORD -> validatePassword(text as String)
    CONFIRM_PASSWORD -> validateConfirmPassword(text as String, comparedText as String)
}

private fun validateEmail(text: String): Condition = when {
    text.isEmpty() -> Condition.Empty
    text.isNotEmail() -> EmailCondition.NotEmail
    else -> Condition.Correct
}

private fun validateNickname(text: String): Condition = when {
    text.isEmpty() -> Condition.Empty
    text.length < NICKNAME_MIN_LENGTH -> NicknameCondition.Short
    else -> Condition.Correct
}

private fun validatePassword(text: String): Condition = when {
    text.isEmpty() -> Condition.Empty
    text.length < PASSWORD_MIN_LENGTH -> PasswordCondition.Short
    else -> Condition.Correct
}

private fun validateConfirmPassword(text: String, comparedText: String): Condition = when {
    text.isEmpty() -> Condition.Empty
    text != comparedText -> ConfirmPasswordCondition.NotMatch
    else -> Condition.Correct
}