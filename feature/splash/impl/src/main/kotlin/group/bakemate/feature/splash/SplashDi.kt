package group.bakemate.feature.splash

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.feature.splash.navigation.SplashScreenProvider
import group.bakemate.feature.splash.presentation.tea.SplashReducer
import group.bakemate.feature.splash.presentation.tea.SplashStore
import group.bakemate.feature.splash.presentation.tea.actor.GetLoggedInStatusActor
import group.bakemate.feature.splash.presentation.tea.actor.SplashNavigationActor
import group.bakemate.feature.splash.presentation.ui.SplashContentScreen
import group.bakemate.feature.welcome.navigation.WelcomeScreenProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val splashModule = module {
    factoryOf(::splashCreationStore)
    factory { SplashScreenProvider(::SplashContentScreen) }
}

internal fun splashCreationStore(
    router: Router,
    welcomeScreenProvider: WelcomeScreenProvider,
): SplashStore {
    return SplashStore(
        reducer = SplashReducer(),
        actors = setOf(
            GetLoggedInStatusActor(),
            SplashNavigationActor(
                router = router,
                welcomeScreenProvider = welcomeScreenProvider,
            )
        )
    )
}