package com.perfomer.checkielite

import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.core.data.datasource.localDataSourceModule
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.feature.main.mainModules
import com.perfomer.checkielite.feature.reviewcreation.reviewCreationModules
import com.perfomer.checkielite.feature.reviewdetails.reviewDetailsModules
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.SingleStackScreenContent
import com.perfomer.checkielite.navigation.StartStackScreenProvider
import com.perfomer.checkielite.navigation.voyager.navigationModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val checkieLiteModules: List<Module>
    get() = appModule + coreModules + featureModules

private val appModule = module {
    singleOf(::SingleStackScreenContent)
    singleOf(::SingleActivityHolder)
    singleOf(::StartStackScreenProvider)
    singleOf(::AndroidExternalRouter) bind ExternalRouter::class
}

private val coreModules
    get() = navigationModule + localDataSourceModule

private val featureModules
    get() = mainModules + reviewCreationModules + reviewDetailsModules