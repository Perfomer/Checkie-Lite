package group.bakemate.core.local.preferences

import group.bakemate.core.local.api.TokenDatasource
import group.bakemate.core.local.preferences.datasource.TokenDatasourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {
    singleOf(::TokenDatasourceImpl) bind TokenDatasource::class
}