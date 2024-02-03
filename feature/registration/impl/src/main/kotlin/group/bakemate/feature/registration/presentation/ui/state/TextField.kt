package group.bakemate.feature.registration.presentation.ui.state

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import group.bakemate.feature.registration.presentation.entity.FieldType

@Stable
internal data class TextField(
    @StringRes
    val name: Int,
    @StringRes
    val errorText: Int?,
    val text: String,
    val type: FieldType,
    val isPassword: Boolean,
)