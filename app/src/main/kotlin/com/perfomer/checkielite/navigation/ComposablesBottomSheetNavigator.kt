package com.perfomer.checkielite.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.compositionUniqueId
import com.composables.core.LocalModalWindow
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.ModalSheetProperties
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.navigation.voyager.navigator.BottomSheetNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.graphics.Color as AndroidColor

internal typealias BottomSheetNavigatorContent = @Composable (bottomSheetNavigator: BottomSheetNavigator) -> Unit

internal val LocalBottomSheetNavigator: ProvidableCompositionLocal<BottomSheetNavigator> = staticCompositionLocalOf {
    error("BottomSheetNavigator not initialized")
}

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
@Composable
internal fun ComposablesBottomSheetNavigator(
    modifier: Modifier = Modifier,
    hideOnBackPress: Boolean = true,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    sheetElevation: Dp = BottomSheetDefaults.Elevation,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    key: String = compositionUniqueId(),
    sheetContent: BottomSheetNavigatorContent = { CurrentScreen() },
    content: BottomSheetNavigatorContent,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        positionalThreshold = { totalDistance -> (totalDistance / 4).coerceIn(128.dp, 384.dp) },
        velocityThreshold = { 24_576.dp },
    )

    Navigator(HiddenBottomSheetScreen, onBackPressed = null, key = key) { navigator ->
        val bottomSheetNavigator = remember(navigator, sheetState, coroutineScope) {
            ComposablesBottomSheetNavigator(navigator, sheetState, coroutineScope)
        }

        CompositionLocalProvider(LocalBottomSheetNavigator provides bottomSheetNavigator) {
            content(bottomSheetNavigator)

            if (navigator.lastItemOrNull != HiddenBottomSheetScreen) {
                ModalBottomSheet(
                    state = sheetState,
                    properties = ModalSheetProperties(dismissOnBackPress = false)
                ) {
                    val window = LocalModalWindow.current
                    LaunchedEffect(Unit) {
                        window.navigationBarColor = AndroidColor.TRANSPARENT
                        with(WindowInsetsControllerCompat(window, window.decorView)) {
                            isAppearanceLightNavigationBars = true
                        }
                    }

                    ClearFocusOnKeyboardClose()

                    Scrim(scrimColor = scrimColor, enter = fadeIn(), exit = fadeOut())

                    BottomSheetNavigatorBackHandler(bottomSheetNavigator, sheetState, hideOnBackPress)

                    Sheet(
                        modifier = modifier
                            .padding(top = 12.dp)
                            .statusBarsPadding()
                            .shadow(sheetElevation, shape)
                            .clip(shape)
                            .background(containerColor)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .imePadding()
                        ) {
                            dragHandle?.invoke()
                            sheetContent(bottomSheetNavigator)
                        }
                    }
                }
            }
        }
    }
}

private class ComposablesBottomSheetNavigator(
    private val navigator: Navigator,
    private val sheetState: ModalBottomSheetState,
    private val coroutineScope: CoroutineScope
) : BottomSheetNavigator,
    Stack<Screen> by navigator {

    override val isVisible: Boolean
        get() = sheetState.currentDetent != SheetDetent.Hidden

    override fun show(screen: Screen) {
        coroutineScope.launch {
            replaceAll(screen)
            sheetState.animateTo(SheetDetent.FullyExpanded)
        }
    }

    override fun hide() {
        coroutineScope.launch {
            if (isVisible) {
                sheetState.animateTo(SheetDetent.Hidden)
                sheetState.currentDetent = SheetDetent.Hidden
                replaceAll(HiddenBottomSheetScreen)
            } else if (sheetState.targetDetent == SheetDetent.Hidden) {
                // Swipe down - sheetState is already hidden here so `isVisible` is false
                replaceAll(HiddenBottomSheetScreen)
            }
        }
    }

    @Composable
    fun saveableState(
        key: String,
        content: @Composable () -> Unit
    ) {
        navigator.saveableState(key, content = content)
    }
}

@Composable
private fun BottomSheetNavigatorBackHandler(
    navigator: ComposablesBottomSheetNavigator,
    sheetState: ModalBottomSheetState,
    hideOnBackPress: Boolean
) {
    BackHandler(enabled = sheetState.currentDetent != SheetDetent.Hidden) {
        if (!navigator.pop() && hideOnBackPress) {
            navigator.hide()
        }
    }
}

private object HiddenBottomSheetScreen : Screen {

    @Composable
    override fun Content() {
        Spacer(modifier = Modifier.height(1.dp))
    }

    private fun readResolve(): Any = HiddenBottomSheetScreen
}
