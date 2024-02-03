package group.bakemate.feature.welcome

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.feature.registration.navigation.RegistrationScreenProvider
import group.bakemate.feature.welcome.navigation.WelcomeScreenProvider
import group.bakemate.feature.welcome.presentation.tea.WelcomeReducer
import group.bakemate.feature.welcome.presentation.tea.WelcomeStore
import group.bakemate.feature.welcome.presentation.tea.actor.WelcomeNavigationActor
import group.bakemate.feature.welcome.presentation.ui.WelcomeContentScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val welcomeModule = module {
    factoryOf(::welcomeCreationStore)
    factory { WelcomeScreenProvider(::WelcomeContentScreen) }
}

internal fun welcomeCreationStore(
    router: Router,
    registrationScreenProvider: RegistrationScreenProvider,
): WelcomeStore {
    return WelcomeStore(
        reducer = WelcomeReducer(),
        actors = setOf(
            WelcomeNavigationActor(
                router = router,
                registrationScreenProvider = registrationScreenProvider,
            )
        )
    )
}

