package group.bakemate.common.ui.bui.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import group.bakemate.common.ui.R
import group.bakemate.common.ui.theme.Grey
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.Red
import group.bakemate.common.ui.theme.RedLight
import group.bakemate.common.ui.theme.WidgetPreview

@Composable
fun BuiOutlinedField(
    modifier: Modifier = Modifier,
    text: String,
    title: String,
    errorText: String? = null,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
        trailingIcon = {
            if (!errorText.isNullOrBlank()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = null,
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Grey,
            errorBorderColor = RedLight,
            focusedLabelColor = RedLight,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        label = { Text(title) },
        isError = !errorText.isNullOrBlank(),
        enabled = isEnabled,
        supportingText = {
            if (!errorText.isNullOrBlank()) {
                Text(
                    text = errorText,
                    fontSize = 12.sp,
                    color = Red,
                )
            }
        },
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
@WidgetPreview
private fun BuiOutlinedFieldPreview() {
    PreviewTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            BuiOutlinedField(title = "Title 1", text = "Text", onValueChange = {})

            BuiOutlinedField(
                title = "Title 1",
                text = "Text",
                errorText = "Никнейм должен содержать хотя бы 6 символов.",
                onValueChange = {},
            )
        }
    }
}