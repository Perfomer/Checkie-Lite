package com.perfomer.checkielite.common.android

import com.perfomer.checkielite.common.android.permissions.PermissionHelper
import com.perfomer.checkielite.common.android.permissions.PermissionHelperImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonAndroidModule = module {
    singleOf(::SingleActivityHolder)
    singleOf(::PermissionHelperImpl) bind PermissionHelper::class
}