package com.perfomer.checkielite.common.ui.cui.widget.field

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

object CuiOutlinedFieldDefaults {

    @Composable
    fun colors(
        unfocusedBorderColor: Color = LocalCuiPalette.current.OutlinePrimary,
        errorBorderColor: Color = LocalCuiPalette.current.BackgroundNegativePrimary,
        focusedLabelColor: Color = LocalCuiPalette.current.TextAccent,
        unfocusedLabelColor: Color = LocalCuiPalette.current.TextSecondary,
        unfocusedPlaceholderColor: Color = LocalCuiPalette.current.TextSecondary,
        focusedPlaceholderColor: Color = LocalCuiPalette.current.TextSecondary,
    ): TextFieldColors {
        return OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = unfocusedBorderColor,
            errorBorderColor = errorBorderColor,
            focusedLabelColor = focusedLabelColor,
            unfocusedLabelColor = unfocusedLabelColor,
            unfocusedPlaceholderColor = unfocusedPlaceholderColor,
            focusedPlaceholderColor = focusedPlaceholderColor,
        )
    }
}

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
    colors: TextFieldColors = CuiOutlinedFieldDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(16.dp),
        colors = colors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        label = title?.let { { Text(title) } },
        placeholder = placeholder?.let { { Text(placeholder) } },
        isError = !errorText.isNullOrBlank(),
        enabled = isEnabled,
        interactionSource = interactionSource,
        supportingText = {
            if (!errorText.isNullOrBlank()) {
                Text(
                    text = errorText,
                    fontSize = 12.sp,
                    color = LocalCuiPalette.current.TextNegative,
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
@WidgetPreview
private fun CuiOutlinedFieldPreview() = CheckieLiteTheme {
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