package group.bakemate.feature.splash.presentation.tea.actor

import group.bakemate.feature.splash.presentation.tea.core.SplashCommand
import group.bakemate.feature.splash.presentation.tea.core.SplashCommand.GetLoggedInStatus
import group.bakemate.feature.splash.presentation.tea.core.SplashEvent
import group.bakemate.feature.splash.presentation.tea.core.SplashEvent.LoggedInStatusSucceed
import group.bakemate.tea.tea.component.Actor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLoggedInStatusActor : Actor<SplashCommand, SplashEvent> {

    override fun act(commands: Flow<SplashCommand>): Flow<SplashEvent> {
        return commands.filterIsInstance<GetLoggedInStatus>()
            .flatMapLatest(::handleCommand)
    }

    private suspend fun handleCommand(command: GetLoggedInStatus): Flow<SplashEvent> {
        delay(2000)
        return flow { emit(LoggedInStatusSucceed(isLoggedIn = false)) }
    }
}