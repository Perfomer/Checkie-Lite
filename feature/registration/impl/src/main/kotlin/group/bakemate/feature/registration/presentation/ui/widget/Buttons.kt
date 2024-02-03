package group.bakemate.feature.registration.presentation.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import group.bakemate.common.ui.bui.button.BuiPrimaryButton
import group.bakemate.common.ui.bui.span.buiPolicyAnnotated
import group.bakemate.common.ui.theme.Grey
import group.bakemate.feature.registration.impl.R
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiState

@Composable
internal fun Buttons(
    state: RegistrationUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BuiPrimaryButton(
            text = stringResource(id = R.string.registration_button_text),
            onClick = onClick,
            isEnabled = state.isInputEnabled,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = buiPolicyAnnotated(text = R.string.registration_button_text),
            fontSize = 12.sp,
            color = Grey,
            textAlign = TextAlign.Center,
            letterSpacing = 0.sp,
            lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}