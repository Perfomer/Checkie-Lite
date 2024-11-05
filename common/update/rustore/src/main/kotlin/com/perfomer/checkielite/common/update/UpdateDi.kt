package com.perfomer.checkielite.common.update

import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.common.update.rustore.RuStoreUpdateManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory

val updateModule = module {
    single { RuStoreAppUpdateManagerFactory.create(get()) }
    singleOf(::RuStoreUpdateManager) bind AppUpdateManager::class
}
