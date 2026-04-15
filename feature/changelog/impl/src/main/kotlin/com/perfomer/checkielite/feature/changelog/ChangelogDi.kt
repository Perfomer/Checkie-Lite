package com.perfomer.checkielite.feature.changelog

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.data.repository.ChangelogRepository
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.changelog.data.datasource.ChangelogRemoteDataSource
import com.perfomer.checkielite.feature.changelog.data.datasource.ChangelogRemoteDataSourceImpl
import com.perfomer.checkielite.feature.changelog.data.repository.ChangelogContentRepositoryImpl
import com.perfomer.checkielite.feature.changelog.domain.repository.ChangelogContentRepository
import com.perfomer.checkielite.feature.changelog.presentation.navigation.ChangelogDestination
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.ChangelogReducer
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.ChangelogStore
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.actor.ChangelogNavigationActor
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.actor.LoadChangelogActor
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.actor.MarkVersionAsSeenActor
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.ChangelogContentScreen
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui.state.ChangelogUiStateMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val changelogModules
    get() = listOf(presentationModule, dataModule)

private val presentationModule = module {
    navigation {
        associate<ChangelogDestination, ChangelogContentScreen>()
    }

    factoryOf(::createChangelogStore)
    factoryOf(::ChangelogContentScreen)
}

private val dataModule = module {
    single { HttpClient(Android) }
    singleOf(::ChangelogRemoteDataSourceImpl) bind ChangelogRemoteDataSource::class
    singleOf(::ChangelogContentRepositoryImpl) bind ChangelogContentRepository::class
}

internal fun createChangelogStore(
    componentContext: ComponentContext,
    router: Router,
    changelogContentRepository: ChangelogContentRepository,
    changelogRepository: ChangelogRepository,
): ChangelogStore {
    return ChangelogStore(
        componentContext = componentContext,
        reducer = ChangelogReducer(),
        uiStateMapper = ChangelogUiStateMapper(),
        actors = setOf(
            ChangelogNavigationActor(router),
            LoadChangelogActor(changelogContentRepository),
            MarkVersionAsSeenActor(changelogRepository),
        ),
    )
}
