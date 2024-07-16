package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSourceImpl
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.file.BackupProgressNotifier
import com.perfomer.checkielite.core.data.datasource.file.BackupProgressObserver
import com.perfomer.checkielite.core.data.datasource.file.BackupProgressObserverImpl
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSourceImpl
import com.perfomer.checkielite.core.data.datasource.file.metadata.BackupMetadataParser
import com.perfomer.checkielite.core.data.datasource.file.metadata.BackupMetadataParserImpl
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val localDataSourceModule = module {
    singleOf(::CheckieLocalDataSourceImpl) bind CheckieLocalDataSource::class

    singleOf(::FileDataSourceImpl) bind FileDataSource::class
    singleOf(::DatabaseDataSourceImpl) bind DatabaseDataSource::class
    singleOf(::PreferencesDataSourceImpl) bind PreferencesDataSource::class

    singleOf(::BackupMetadataParserImpl) bind BackupMetadataParser::class
    singleOf(::BackupProgressObserverImpl) binds arrayOf(BackupProgressObserver::class, BackupProgressNotifier::class)

    singleOf(CheckieDatabase::getInstance)
}