package com.perfomer.checkielite.common.ui.cui.widget.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.isRenderEffectSupported
import com.perfomer.checkielite.common.ui.cui.modifier.thenIf
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.LocalLiquidGlassEnabled

/** Records only screen content, so the toolbar never samples its own text or controls. */
@Composable
fun CuiGlassScaffold(
    topBar: @Composable () -> Unit,
    containerColor: Color = LocalCuiPalette.current.BackgroundPrimary,
    toolbarColor: Color = LocalCuiPalette.current.BackgroundPrimary,
    toolbarBackgroundProgress: () -> Float = { 1F },
    content: @Composable (PaddingValues) -> Unit,
) {
    val backdrop = rememberLayerBackdrop {
        drawRect(toolbarColor)
        drawContent()
    }
    val glassEnabled = LocalLiquidGlassEnabled.current && isRenderEffectSupported()

    Scaffold(
        containerColor = containerColor,
        topBar = {
            Box {
                CuiGlassToolbarBackground(
                    backdrop = backdrop,
                    backgroundColor = toolbarColor,
                    progress = toolbarBackgroundProgress,
                    modifier = Modifier.matchParentSize()
                )
                topBar()
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .thenIf(glassEnabled) { layerBackdrop(backdrop) }
        ) {
            content(contentPadding)
        }
    }
}
