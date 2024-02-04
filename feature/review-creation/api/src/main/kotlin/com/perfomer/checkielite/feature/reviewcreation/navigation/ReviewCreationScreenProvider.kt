package com.perfomer.checkielite.feature.reviewcreation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface ReviewCreationScreenProvider {

    operator fun invoke(): CheckieScreen
}