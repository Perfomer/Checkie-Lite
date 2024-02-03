package group.bakemate.feature.registration.presentation.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import group.bakemate.common.ui.bui.field.BuiOutlinedField
import group.bakemate.feature.registration.presentation.entity.FieldType
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiState

@Composable
internal fun Fields(
    state: RegistrationUiState,
    onTextChange: (String, FieldType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.textFields.forEach { field ->
            BuiOutlinedField(
                text = field.text,
                title = stringResource(id = field.name),
                errorText = field.errorText?.let { text -> stringResource(id = text) },
                isPassword = field.isPassword,
                isEnabled = state.isInputEnabled,
                onValueChange = { value -> onTextChange(value.trim(), field.type) },
            )
        }
    }
}