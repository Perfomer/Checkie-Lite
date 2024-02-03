package group.bakemate.feature.production.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import group.bakemate.core.navigation.voyager.BaseScreen

internal class ProductionContentScreen : BaseScreen() {

    @Composable
    override fun Screen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
            Text(text = "Production", fontSize = 36.sp)
        }
    }
}