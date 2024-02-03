package group.bakemate.feature.registration.presentation.tea.actor

import group.bakemate.common.pure.util.flow
import group.bakemate.feature.registration.domain.RegistrationRepository
import group.bakemate.feature.registration.presentation.tea.core.RegistrationCommand
import group.bakemate.feature.registration.presentation.tea.core.RegistrationCommand.RegisterUser
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent.RegistrationError
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent.RegistrationSucceed
import group.bakemate.tea.tea.component.Actor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class RegisterUserActor(
    private val registrationRepository: RegistrationRepository
) : Actor<RegistrationCommand, RegistrationEvent> {

    override fun act(commands: Flow<RegistrationCommand>): Flow<RegistrationEvent> {
        return commands.filterIsInstance<RegisterUser>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: RegisterUser): Flow<RegistrationEvent> {
        return flow { registrationRepository.registerUser(command.registerData) }
            .map { RegistrationSucceed }
            .catch { error -> RegistrationError(error) }
    }
}