package group.bakemate.feature.registration.presentation.tea.actor

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.feature.registration.presentation.tea.core.RegistrationCommand
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent
import group.bakemate.feature.registration.presentation.tea.core.RegistrationNavigationCommand
import group.bakemate.feature.registration.presentation.tea.core.RegistrationNavigationCommand.Exit
import group.bakemate.tea.tea.component.Actor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class RegistrationNavigationActor(
    private val router: Router,
) : Actor<RegistrationCommand, RegistrationEvent> {

    override fun act(commands: Flow<RegistrationCommand>): Flow<RegistrationEvent> {
        return commands.filterIsInstance<RegistrationNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: RegistrationNavigationCommand): RegistrationEvent? {
        when (command) {
            is Exit -> router.exit()
        }

        return null
    }
}