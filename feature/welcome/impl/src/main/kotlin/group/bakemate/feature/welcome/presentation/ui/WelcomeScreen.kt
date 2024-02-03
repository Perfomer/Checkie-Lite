package group.bakemate.feature.welcome.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.ScreenPreview
import group.bakemate.common.ui.theme.White
import group.bakemate.feature.welcome.presentation.ui.widget.Buttons
import group.bakemate.feature.welcome.presentation.ui.widget.Description
import group.bakemate.feature.welcome.presentation.ui.widget.Header

@Composable
internal fun WelcomeScreen(
    onLoginButtonClick: () -> Unit,
    onRegisterButtonClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .background(White)
            .padding(horizontal = 24.dp)
            .navigationBarsPadding()
    ) {
        Column {
            Header()
            Description()
        }

        Row(
            modifier = Modifier.weight(1F, false)
        ) {
            Buttons(
                onLoginButtonClick = onLoginButtonClick,
                onRegisterButtonClick = onRegisterButtonClick,
            )
        }
    }
}


@ScreenPreview
@Composable
internal fun WelcomeScreenPreviewFilled() = PreviewTheme {
    WelcomeScreen(
        onLoginButtonClick = {},
        onRegisterButtonClick = {},
    )
}