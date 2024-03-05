package com.perfomer.checkielite.common.ui.cui.widget.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiOutlinedField(
    modifier: Modifier = Modifier,
    text: String,
    title: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = LocalCuiPalette.current.OutlinePrimary,
            errorBorderColor = LocalCuiPalette.current.BackgroundNegative,
            focusedLabelColor = LocalCuiPalette.current.TextAccent,
            unfocusedLabelColor = LocalCuiPalette.current.TextSecondary,
            unfocusedPlaceholderColor = LocalCuiPalette.current.TextSecondary,
            focusedPlaceholderColor = LocalCuiPalette.current.TextSecondary,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        label = title?.let { { Text(title) } },
        placeholder = placeholder?.let { { Text(placeholder) } },
        isError = !errorText.isNullOrBlank(),
        enabled = isEnabled,
        supportingText = {
            if (!errorText.isNullOrBlank()) {
                Text(
                    text = errorText,
                    fontSize = 12.sp,
                    color = CuiColorToken.Red,
                )
            }
        },
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
            CuiOutlinedField(title = "Title 1", text = "Text", onValueChange = {})

            CuiOutlinedField(
                title = "Title 1",
                text = "Text",
                errorText = "Никнейм должен содержать хотя бы 6 символов.",
                onValueChange = {},
            )
        }
    }
}