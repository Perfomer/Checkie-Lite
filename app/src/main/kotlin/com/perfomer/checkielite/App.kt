package com.perfomer.checkielite

import android.app.Application
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
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
    private val localDataSource: CheckieLocalDataSource by inject()

    override fun onCreate() {
        super.onCreate()

        setupKoin()

        appScope.launch {
            runCatching {
                localDataSource.dropSyncing()
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