package group.bakemate.feature.registration.presentation.ui.factory

import group.bakemate.feature.registration.impl.R
import group.bakemate.feature.registration.presentation.tea.core.RegistrationState
import group.bakemate.feature.registration.presentation.entity.Condition
import group.bakemate.feature.registration.presentation.entity.ConfirmPasswordCondition
import group.bakemate.feature.registration.presentation.entity.EmailCondition
import group.bakemate.feature.registration.presentation.entity.NicknameCondition
import group.bakemate.feature.registration.presentation.entity.PasswordCondition
import group.bakemate.feature.registration.presentation.entity.FieldType.*
import group.bakemate.feature.registration.presentation.ui.state.TextField

internal class RegistrationFactory {

    fun create(state: RegistrationState): List<TextField> = with(state) {
        val emailTextField = TextField(
            name = R.string.registration_field_email,
            text = email,
            errorText = setErrorMessage(conditions[EMAIL]),
            type = EMAIL,
            isPassword = false,
        )

        val nickNameTextField = TextField(
            name = R.string.registration_field_nickname,
            text = nickname,
            errorText = setErrorMessage(conditions[NICKNAME]),
            type = NICKNAME,
            isPassword = false,
        )

        val passwordTextField = TextField(
            name = R.string.registration_field_password,
            text = password,
            errorText = setErrorMessage(conditions[PASSWORD]),
            type = PASSWORD,
            isPassword = true,
        )

        val confirmPasswordTextField = TextField(
            name = R.string.registration_field_confirm_password,
            text = confirmPassword,
            errorText = setErrorMessage(conditions[CONFIRM_PASSWORD]),
            type = CONFIRM_PASSWORD,
            isPassword = true,
        )

        return listOf(emailTextField, nickNameTextField, passwordTextField, confirmPasswordTextField)
    }

    private fun setErrorMessage(condition: Condition?): Int? = when(condition) {
        is ConfirmPasswordCondition.NotMatch -> R.string.registration_field_confirm_password_error
        is EmailCondition.NotEmail -> R.string.registration_field_email_error
        is Condition.Empty -> R.string.registration_field_empty_error
        is NicknameCondition.Short -> R.string.registration_field_nickname_error
        is PasswordCondition.Short -> R.string.registration_field_password_error
        else -> null
    }
}