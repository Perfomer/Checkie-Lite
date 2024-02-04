package com.perfomer.checkielite.feature.reviewdetails

import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.presentation.ReviewDetailsContentScreen
import org.koin.dsl.module

val reviewDetailsModule = module {
    factory { ReviewDetailsScreenProvider(::ReviewDetailsContentScreen) }
}