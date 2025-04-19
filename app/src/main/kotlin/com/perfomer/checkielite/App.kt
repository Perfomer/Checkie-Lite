package com.perfomer.checkielite

import android.app.Application
import com.perfomer.checkielite.appinfo.AppInfoInitializer
import com.perfomer.checkielite.core.data.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val appRepository: AppRepository by inject()

    override fun onCreate() {
        super.onCreate()

        setupKoin()

        AppInfoInitializer.initialize(this)

        appScope.launch {
            runCatching {
                appRepository.dropSyncing()
            }
        }
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(checkieLiteModules)
        }
    }
}