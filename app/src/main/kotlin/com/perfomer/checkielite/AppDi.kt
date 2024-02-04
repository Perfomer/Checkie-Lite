package com.perfomer.checkielite

import group.bakemate.common.android.SingleActivityHolder
import group.bakemate.core.local.preferences.preferencesModule
import group.bakemate.core.navigation.voyager.navigationModule
import group.bakemate.core.storage.impl.storageModule
import group.bakemate.feature.main.mainModule
import group.bakemate.feature.orders.ordersModule
import group.bakemate.feature.production.productionModule
import com.perfomer.checkielite.navigation.SingleStackScreenContent
import com.perfomer.checkielite.navigation.StartStackScreenProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val checkieLiteModules: List<Module>
    get() = appModule + coreModules + featureModules

private val appModule = module {
    singleOf(::SingleStackScreenContent)
    singleOf(::SingleActivityHolder)
    singleOf(::StartStackScreenProvider)
}

private val coreModules
    get() = navigationModule + storageModule + preferencesModule

private val featureModules
    get() = mainModule + ordersModule + productionModule