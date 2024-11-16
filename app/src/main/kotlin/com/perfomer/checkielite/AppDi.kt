package com.perfomer.checkielite

import com.perfomer.checkielite.appinfo.AppInfoProviderImpl
import com.perfomer.checkielite.common.android.commonAndroidModule
import com.perfomer.checkielite.common.pure.appInfo.AppInfoProvider
import com.perfomer.checkielite.common.update.updateModule
import com.perfomer.checkielite.core.data.localDataSourceModule
import com.perfomer.checkielite.core.navigation.ExternalRouter
import com.perfomer.checkielite.feature.gallery.galleryModules
import com.perfomer.checkielite.feature.main.mainModules
import com.perfomer.checkielite.feature.reviewcreation.reviewCreationModules
import com.perfomer.checkielite.feature.reviewdetails.reviewDetailsModules
import com.perfomer.checkielite.feature.search.searchModules
import com.perfomer.checkielite.feature.settings.settingsModules
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.BackupNavigationManager
import com.perfomer.checkielite.navigation.StartScreenProvider
import com.perfomer.checkielite.navigation.voyager.navigationModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val checkieLiteModules: List<Module>
    get() = appModule + coreModules + commonModules + featureModules

private val appModule = module {
    singleOf(::AndroidExternalRouter) bind ExternalRouter::class
    singleOf(::AppInfoProviderImpl) bind AppInfoProvider::class

    singleOf(::BackupNavigationManager)
    singleOf(::StartScreenProvider)
}

private val coreModules
    get() = listOf(
        localDataSourceModule,
    )

private val commonModules
    get() = listOf(
        commonAndroidModule,
        navigationModule,
        com.perfomer.checkielite.navigation.di.navigationModule,
        updateModule,
    )

private val featureModules
    get() = listOf(
        galleryModules,
        mainModules,
        reviewCreationModules,
        reviewDetailsModules,
        searchModules,
        settingsModules,
    ).flatten()