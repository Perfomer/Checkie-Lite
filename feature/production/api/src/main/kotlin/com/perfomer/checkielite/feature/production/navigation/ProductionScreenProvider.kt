package com.perfomer.checkielite.feature.production.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface ProductionScreenProvider {

    operator fun invoke(): CheckieScreen
}