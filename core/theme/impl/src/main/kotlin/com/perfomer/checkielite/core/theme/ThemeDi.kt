package com.perfomer.checkielite.core.theme

import com.perfomer.checkielite.core.theme.manager.ThemeManager
import com.perfomer.checkielite.core.theme.manager.ThemeManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val themeModule = module {
    singleOf(::ThemeManagerImpl) bind ThemeManager::class
}