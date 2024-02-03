package group.bakemate.feature.production.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface ProductionScreenProvider {

    operator fun invoke(): CheckieScreen
}