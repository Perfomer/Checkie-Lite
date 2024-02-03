package group.bakemate.feature.welcome.presentation.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import group.bakemate.common.ui.bui.span.buiAppNameAnnotated
import group.bakemate.feature.welcome.impl.R

@Composable
internal fun Description() {
    Text(
        text = AnnotatedString(stringResource(id = R.string.welcome_description)) + buiAppNameAnnotated(),
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}