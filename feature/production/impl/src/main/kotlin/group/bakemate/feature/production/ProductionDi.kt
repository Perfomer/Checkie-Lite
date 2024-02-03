package group.bakemate.feature.production

import group.bakemate.feature.production.navigation.ProductionScreenProvider
import group.bakemate.feature.production.presentation.ProductionContentScreen
import org.koin.dsl.module

val productionModule = module {
    factory { ProductionScreenProvider(::ProductionContentScreen) }
}