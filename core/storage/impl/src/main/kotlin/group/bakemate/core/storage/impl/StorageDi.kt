package group.bakemate.core.storage.impl

import group.bakemate.core.storage.api.UserStorage
import group.bakemate.core.storage.impl.storage.UserStorageImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val storageModule = module {
    singleOf(::UserStorageImpl) bind UserStorage::class
}