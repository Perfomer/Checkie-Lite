package com.perfomer.checkielite

import android.app.Application
import androidx.appfunctions.service.AppFunctionConfiguration
import com.perfomer.checkielite.appinfo.AppInfoInitializer
import com.perfomer.checkielite.core.data.repository.AppRepository
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.feature.appfunctions.checkieAppFunctionConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(), AppFunctionConfiguration.Provider {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val appRepository: AppRepository by inject()
    private val reviewRepository: ReviewRepository by inject()

    override val appFunctionConfiguration: AppFunctionConfiguration
        get() = checkieAppFunctionConfiguration(reviewRepository)

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
