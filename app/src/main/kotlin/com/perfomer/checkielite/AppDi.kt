package com.perfomer.checkielite

import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.navigation.voyager.navigationModule
import com.perfomer.checkielite.core.storage.storageModule
import com.perfomer.checkielite.feature.main.mainModule
import com.perfomer.checkielite.feature.orders.ordersModule
import com.perfomer.checkielite.feature.production.productionModule
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
    get() = navigationModule + storageModule

private val featureModules
    get() = mainModule + ordersModule + productionModule