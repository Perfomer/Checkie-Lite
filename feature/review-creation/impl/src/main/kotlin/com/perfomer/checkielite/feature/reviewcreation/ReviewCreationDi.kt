package com.perfomer.checkielite.feature.reviewcreation

import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.ReviewCreationContentScreen
import org.koin.dsl.module

val reviewCreationModule = module {
    factory { ReviewCreationScreenProvider(::ReviewCreationContentScreen) }
}