package group.bakemate.feature.welcome.presentation.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import group.bakemate.common.ui.bui.button.BuiPrimaryButton
import group.bakemate.common.ui.bui.button.BuiSecondaryButton
import group.bakemate.feature.welcome.impl.R

@Composable
internal fun Buttons(
    onLoginButtonClick: () -> Unit,
    onRegisterButtonClick: () -> Unit,
) {
    Column {
        BuiPrimaryButton(
            text = stringResource(id = R.string.welcome_account),
            onClick = onLoginButtonClick,
        )

        Spacer(modifier = Modifier.size(12.dp))

        BuiSecondaryButton(
            text = stringResource(id = R.string.welcome_registration),
            onClick = onRegisterButtonClick,
        )

        Spacer(modifier = Modifier.size(20.dp))
    }

}