package com.perfomer.checkielite.feature.production

import com.perfomer.checkielite.feature.production.navigation.ProductionScreenProvider
import com.perfomer.checkielite.feature.production.presentation.ProductionContentScreen
import org.koin.dsl.module

val productionModule = module {
    factory { ProductionScreenProvider(::ProductionContentScreen) }
}