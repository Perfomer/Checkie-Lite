package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.data.datasource.room.CheckieDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDataSourceModule = module {
    singleOf(::CheckieLocalDataSourceImpl) bind CheckieLocalDataSource::class

    single {
        CheckieDatabase.getInstance(
            appContext = androidContext(),
            databaseName = "CheckieDatabase",
            inMemory = false,
        )
    }
}