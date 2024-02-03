package group.bakemate.feature.registration.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import group.bakemate.common.ui.bui.text.BuiTitleText
import group.bakemate.common.ui.bui.toolbar.BuiToolbar
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.WidgetPreview
import group.bakemate.feature.registration.impl.R
import group.bakemate.feature.registration.presentation.entity.FieldType
import group.bakemate.feature.registration.presentation.ui.entity.mockRegistrationUiState
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiState
import group.bakemate.feature.registration.presentation.ui.widget.Buttons
import group.bakemate.feature.registration.presentation.ui.widget.Fields

@Composable
internal fun RegistrationScreen(
    state: RegistrationUiState,
    onTextChange: (String, FieldType) -> Unit,
    onBackPressed: () -> Unit,
    onButtonClick: () -> Unit,
) {
    val horizontalPadding = 24.dp
    val spacePadding = 24.dp

    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxHeight()
            .verticalScroll(state = rememberScrollState())
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column() {
            BuiToolbar(
                onBackPress = onBackPressed,
                modifier = Modifier.padding(bottom = spacePadding),
            )

            BuiTitleText(
                text = stringResource(id = R.string.registration_title),
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .padding(bottom = spacePadding),
            )

            Fields(
                state = state,
                onTextChange = onTextChange,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
        }

        Buttons(
            state = state,
            onClick = onButtonClick,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = spacePadding)
        )
    }
}

@Composable
@WidgetPreview
fun RegistrationScreenPreviewFilled() {
    PreviewTheme {
        RegistrationScreen(
            state = RegistrationUiState(
                textFields = mockRegistrationUiState.textFields,
                isInputEnabled = mockRegistrationUiState.isInputEnabled,
            ),
            onTextChange = { _, _ -> },
            onBackPressed = {},
            onButtonClick = {},
        )
    }
}