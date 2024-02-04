package com.perfomer.checkielite.feature.main

import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.feature.main.presentation.tea.MainReducer
import com.perfomer.checkielite.feature.main.presentation.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.ui.MainContentScreen
import com.perfomer.checkielite.feature.main.presentation.ui.state.MainUiStateMapper
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