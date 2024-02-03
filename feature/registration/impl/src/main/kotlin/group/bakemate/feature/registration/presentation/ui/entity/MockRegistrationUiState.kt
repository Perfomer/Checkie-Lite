package group.bakemate.feature.registration.presentation.ui.entity

import group.bakemate.feature.registration.impl.R
import group.bakemate.feature.registration.presentation.entity.FieldType.CONFIRM_PASSWORD
import group.bakemate.feature.registration.presentation.entity.FieldType.EMAIL
import group.bakemate.feature.registration.presentation.entity.FieldType.NICKNAME
import group.bakemate.feature.registration.presentation.entity.FieldType.PASSWORD
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiState
import group.bakemate.feature.registration.presentation.ui.state.TextField

private val emailTextField = TextField(
    name = R.string.registration_field_email,
    text = "awake@@",
    errorText = R.string.registration_field_email_error,
    isPassword = false,
    type = EMAIL,
)

private val nickNameTextField = TextField(
    name = R.string.registration_field_nickname,
    text = "awake",
    errorText = null,
    isPassword = false,
    type = NICKNAME
)

private val passwordTextField = TextField(
    name = R.string.registration_field_password,
    text = "123",
    errorText = R.string.registration_field_password_error,
    isPassword = true,
    type = PASSWORD
)

private val confirmPasswordTextField = TextField(
    name = R.string.registration_field_confirm_password,
    text = "123",
    errorText = null,
    isPassword = true,
    type = CONFIRM_PASSWORD
)

internal val mockRegistrationUiState = RegistrationUiState(
    textFields = listOf(emailTextField, nickNameTextField, passwordTextField, confirmPasswordTextField),
    isInputEnabled = true,
)
