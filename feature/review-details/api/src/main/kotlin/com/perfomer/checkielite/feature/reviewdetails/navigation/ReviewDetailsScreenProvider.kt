package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface ReviewDetailsScreenProvider {

    operator fun invoke(): CheckieScreen
}