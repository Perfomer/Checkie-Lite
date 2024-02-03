package group.bakemate.feature.registration

import com.perfomer.checkielite.core.navigation.api.Router
import group.bakemate.feature.registration.data.RegistrationRepositoryImpl
import group.bakemate.feature.registration.domain.RegistrationRepository
import group.bakemate.feature.registration.navigation.RegistrationScreenProvider
import group.bakemate.feature.registration.presentation.tea.RegistrationReducer
import group.bakemate.feature.registration.presentation.tea.RegistrationStore
import group.bakemate.feature.registration.presentation.tea.actor.RegisterUserActor
import group.bakemate.feature.registration.presentation.tea.actor.RegistrationNavigationActor
import group.bakemate.feature.registration.presentation.ui.RegistrationContentScreen
import group.bakemate.feature.registration.presentation.ui.factory.RegistrationFactory
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val registrationModule = module {
    factoryOf(::registrationCreationStore)
    factory { RegistrationScreenProvider(::RegistrationContentScreen) }
    singleOf(::RegistrationRepositoryImpl) bind RegistrationRepository::class
}

internal fun registrationCreationStore(
    router: Router,
    registrationRepository: RegistrationRepository,
): RegistrationStore {
    return RegistrationStore(
        reducer = RegistrationReducer(),
        uiStateMapper = RegistrationUiStateMapper(
            registrationFactory = RegistrationFactory(),
        ),
        actors = setOf(
            RegisterUserActor(registrationRepository = registrationRepository),
            RegistrationNavigationActor(router = router)
        )
    )
}