package group.bakemate.feature.splash.presentation.ui

import androidx.compose.runtime.Composable
import group.bakemate.common.ui.tea.TeaComposable
import group.bakemate.common.ui.tea.store
import group.bakemate.core.navigation.voyager.BaseScreen
import group.bakemate.feature.splash.presentation.tea.SplashStore
import group.bakemate.feature.splash.presentation.tea.core.SplashUiEvent.AnimationEnd

internal class SplashContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<SplashStore>()) {
        accept(AnimationEnd)
        SplashScreen()
    }
}