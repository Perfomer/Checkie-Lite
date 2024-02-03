package group.bakemate.feature.registration.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import group.bakemate.common.ui.tea.TeaComposable
import group.bakemate.common.ui.tea.acceptable
import group.bakemate.common.ui.tea.store
import group.bakemate.core.navigation.voyager.BaseScreen
import group.bakemate.feature.registration.presentation.tea.RegistrationStore
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.OnBackPressed
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.OnButtonClick
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.OnTextChange

internal class RegistrationContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<RegistrationStore>()) { state ->
        BackHandler { accept(OnBackPressed)}

        RegistrationScreen(
            state = state,
            onTextChange = acceptable(::OnTextChange),
            onBackPressed = acceptable(OnBackPressed),
            onButtonClick = acceptable(OnButtonClick)
        )
    }
}