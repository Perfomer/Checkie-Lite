package group.bakemate.feature.splash.presentation.tea.actor

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.feature.splash.presentation.tea.core.SplashCommand
import group.bakemate.feature.splash.presentation.tea.core.SplashEvent
import group.bakemate.feature.splash.presentation.tea.core.SplashNavigationCommand
import group.bakemate.feature.splash.presentation.tea.core.SplashNavigationCommand.OpenMain
import group.bakemate.feature.splash.presentation.tea.core.SplashNavigationCommand.OpenWelcome
import group.bakemate.feature.welcome.navigation.WelcomeScreenProvider
import group.bakemate.tea.tea.component.Actor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class SplashNavigationActor(
    private val router: Router,
    private val welcomeScreenProvider: WelcomeScreenProvider,
) : Actor<SplashCommand, SplashEvent> {


    override fun act(commands: Flow<SplashCommand>): Flow<SplashEvent> {
        return commands.filterIsInstance<SplashNavigationCommand>()
            .flatMapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: SplashNavigationCommand): Flow<SplashEvent> {
        when (command) {
            is OpenWelcome -> router.replace(welcomeScreenProvider())
            is OpenMain -> TODO()
        }

        return emptyFlow()
    }
}