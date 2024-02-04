package com.perfomer.checkielite.feature.reviewcreation

import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.ReviewCreationContentScreen
import org.koin.dsl.module

val reviewCreationModule = module {
    factory { ReviewCreationScreenProvider(::ReviewCreationContentScreen) }
}