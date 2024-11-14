package com.perfomer.checkielite.newnavigation.screenb

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.newnavigation.screenb.tea.ScreenBEffect
import com.perfomer.checkielite.newnavigation.screenb.tea.ScreenBStore
import com.perfomer.checkielite.newnavigation.screenb.tea.ScreenBUiEvent

class ScreenB(
    private val store: ScreenBStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        val context = LocalContext.current

        EffectHandler { effect ->
            when (effect) {
                ScreenBEffect.ShowToast -> Toast.makeText(context, state.text, Toast.LENGTH_SHORT).show()
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(40.dp)
        ) {
            Text("Screen B: ${state.text}")

            Button(
                onClick = { accept(ScreenBUiEvent.OnBackClick) }
            ) {
                Text("Go back")
            }

            Button(
                onClick = { accept(ScreenBUiEvent.OnShowToastClick) }
            ) {
                Text("Show toast")
            }
        }
    }
}