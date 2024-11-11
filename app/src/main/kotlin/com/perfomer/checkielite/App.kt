package com.perfomer.checkielite

import android.app.Application
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.newnavigation.ADestination
import com.perfomer.checkielite.newnavigation.BDestination
import com.perfomer.checkielite.newnavigation.NavigationRegistry
import com.perfomer.checkielite.newnavigation.associate
import com.perfomer.checkielite.newnavigation.screena.ScreenA
import com.perfomer.checkielite.newnavigation.screenb.ScreenB
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

        NavigationRegistry {
            associate<ADestination, ScreenA>()
            associate<BDestination, ScreenB>()
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