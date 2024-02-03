package group.bakemate.feature.registration.presentation.tea

import group.bakemate.core.entity.RegisterData
import group.bakemate.feature.registration.presentation.entity.Condition.Correct
import group.bakemate.feature.registration.presentation.entity.FieldType.CONFIRM_PASSWORD
import group.bakemate.feature.registration.presentation.entity.FieldType.EMAIL
import group.bakemate.feature.registration.presentation.entity.FieldType.NICKNAME
import group.bakemate.feature.registration.presentation.entity.FieldType.PASSWORD
import group.bakemate.feature.registration.presentation.tea.core.RegistrationCommand
import group.bakemate.feature.registration.presentation.tea.core.RegistrationCommand.RegisterUser
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEffect
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent.RegistrationError
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent.RegistrationSucceed
import group.bakemate.feature.registration.presentation.tea.core.RegistrationNavigationCommand.Exit
import group.bakemate.feature.registration.presentation.tea.core.RegistrationState
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.Initialize
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.OnBackPressed
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.OnButtonClick
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.OnTextChange
import group.bakemate.feature.registration.presentation.utils.validate
import group.bakemate.tea.tea.dsl.DslReducer

internal class RegistrationReducer :
    DslReducer<RegistrationCommand, RegistrationEffect, RegistrationEvent, RegistrationState>() {

    override fun reduce(event: RegistrationEvent) = when (event) {
        is Initialize -> state {
            copy(
                email = state.email,
                nickname = state.nickname,
                password = state.password,
                confirmPassword = state.confirmPassword,
            )
        }

        is OnTextChange -> {
            when (event.type) {
                EMAIL -> state { copy(email = event.text) }
                NICKNAME -> state { copy(nickname = event.text) }
                PASSWORD -> state { copy(password = event.text) }
                CONFIRM_PASSWORD -> state { copy(confirmPassword = event.text) }
            }
        }

        is OnBackPressed -> commands(Exit)

        is RegistrationError -> state { copy(isSending = false) }

        is RegistrationSucceed -> Unit

        is OnButtonClick -> {
            val conditions = mapOf(
                EMAIL to validate(EMAIL, state.email),
                NICKNAME to validate(NICKNAME, state.nickname),
                PASSWORD to validate(PASSWORD, state.password),
                CONFIRM_PASSWORD to validate(CONFIRM_PASSWORD, state.confirmPassword, state.password),
            )

            val areFieldsCorrect = conditions.values.all { condition -> condition == Correct }

            state { copy(conditions = conditions, isSending = true) }

            if (areFieldsCorrect) {
                val registerData = RegisterData(
                    name = state.nickname,
                    email = state.email,
                    password = state.password,
                )

                commands(RegisterUser(registerData = registerData))
            }

            Unit
        }
    }
}