package group.bakemate.feature.main

import group.bakemate.feature.main.navigation.MainScreenProvider
import group.bakemate.feature.main.presentation.tea.MainReducer
import group.bakemate.feature.main.presentation.tea.MainStore
import group.bakemate.feature.main.presentation.ui.MainContentScreen
import group.bakemate.feature.main.presentation.ui.state.MainUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainModule = module {
    factoryOf(::mainCreationStore)
    factory { MainScreenProvider { MainContentScreen() } }
}

internal fun mainCreationStore(): MainStore {
    return MainStore(
        reducer = MainReducer(),
        uiStateMapper = MainUiStateMapper(),
        actors = emptySet(),
    )
}