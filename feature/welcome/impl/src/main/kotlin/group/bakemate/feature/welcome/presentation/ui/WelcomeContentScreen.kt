package group.bakemate.feature.welcome.presentation.ui

import androidx.compose.runtime.Composable
import group.bakemate.common.ui.tea.TeaComposable
import group.bakemate.common.ui.tea.acceptable
import group.bakemate.common.ui.tea.store
import group.bakemate.core.navigation.voyager.BaseScreen
import group.bakemate.feature.welcome.presentation.tea.WelcomeStore
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeUiEvent.OnLoginButtonClick
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeUiEvent.OnRegisterButtonClick

internal class WelcomeContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<WelcomeStore>()) {
        WelcomeScreen(
            onRegisterButtonClick = acceptable(OnRegisterButtonClick),
            onLoginButtonClick = acceptable(OnLoginButtonClick),
        )
    }
}