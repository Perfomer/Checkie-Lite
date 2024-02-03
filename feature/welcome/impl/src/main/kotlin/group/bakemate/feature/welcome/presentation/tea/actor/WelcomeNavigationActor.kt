package group.bakemate.feature.welcome.presentation.tea.actor

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.feature.registration.navigation.RegistrationScreenProvider
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeCommand
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeEvent
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeNavigationCommand
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeNavigationCommand.OpenLogin
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeNavigationCommand.OpenRegistration
import group.bakemate.tea.tea.component.Actor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class WelcomeNavigationActor(
    private val router: Router,
    private val registrationScreenProvider: RegistrationScreenProvider,
) : Actor<WelcomeCommand, WelcomeEvent> {


    override fun act(commands: Flow<WelcomeCommand>): Flow<WelcomeEvent> {
        return commands.filterIsInstance<WelcomeNavigationCommand>()
            .flatMapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: WelcomeNavigationCommand): Flow<WelcomeEvent> {
        when (command) {
            OpenLogin -> TODO()
            OpenRegistration -> router.navigate(registrationScreenProvider())
        }

        return emptyFlow()
    }
}