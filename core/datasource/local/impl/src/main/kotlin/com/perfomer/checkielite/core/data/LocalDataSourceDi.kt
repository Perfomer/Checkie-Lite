package com.perfomer.checkielite.core.data

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSourceImpl
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSourceImpl
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSourceImpl
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadataParser
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadataParserImpl
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSourceImpl
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.data.repository.BackupRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDataSourceModule = module {
    singleOf(::CheckieLocalDataSourceImpl) bind CheckieLocalDataSource::class

    singleOf(::FileDataSourceImpl) bind FileDataSource::class
    singleOf(::DatabaseDataSourceImpl) bind DatabaseDataSource::class
    singleOf(::PreferencesDataSourceImpl) bind PreferencesDataSource::class

    singleOf(::BackupMetadataParserImpl) bind BackupMetadataParser::class
    singleOf(::BackupRepositoryImpl) bind BackupRepository::class

    singleOf(CheckieDatabase::getInstance)
}