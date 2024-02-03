package group.bakemate.common.ui.bui.span

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import group.bakemate.common.ui.R
import group.bakemate.common.ui.theme.OrangeDark

@Composable
fun buiPolicyAnnotated(@StringRes text: Int): AnnotatedString {
    return AnnotatedString(text = stringResource(id = R.string.app_policy_text, stringResource(id = text))) +
            AnnotatedString(text = " ") +
            AnnotatedString(
                text = stringResource(id = R.string.app_policy_clickable),
                spanStyle = SpanStyle(color = OrangeDark, textDecoration = TextDecoration.Underline)
            )
}