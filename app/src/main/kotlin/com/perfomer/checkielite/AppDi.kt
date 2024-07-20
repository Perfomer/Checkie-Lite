package com.perfomer.checkielite

import com.perfomer.checkielite.appinfo.AppInfoProviderImpl
import com.perfomer.checkielite.common.android.commonAndroidModule
import com.perfomer.checkielite.common.pure.appInfo.AppInfoProvider
import com.perfomer.checkielite.core.data.localDataSourceModule
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.feature.gallery.galleryModules
import com.perfomer.checkielite.feature.main.mainModules
import com.perfomer.checkielite.feature.reviewcreation.reviewCreationModules
import com.perfomer.checkielite.feature.reviewdetails.reviewDetailsModules
import com.perfomer.checkielite.feature.search.searchModules
import com.perfomer.checkielite.feature.settings.settingsModules
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.voyager.navigationModule
import com.perfomer.checkielite.update.AppUpdateManager
import com.perfomer.checkielite.update.RuStoreUpdateManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory

val checkieLiteModules: List<Module>
    get() = appModule + coreModules + commonModules + featureModules

private val appModule = module {
    singleOf(::AndroidExternalRouter) bind ExternalRouter::class
    singleOf(::AppInfoProviderImpl) bind AppInfoProvider::class

    single { RuStoreAppUpdateManagerFactory.create(get()) }
    singleOf(::RuStoreUpdateManager) bind AppUpdateManager::class
}

private val coreModules
    get() = listOf(
        localDataSourceModule,
    )

private val commonModules
    get() = listOf(
        commonAndroidModule,
        navigationModule,
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