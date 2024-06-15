package com.perfomer.checkielite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.ui.cui.widget.scrim.NavBarScrim
import com.perfomer.checkielite.common.ui.cui.widget.sheet.CuiDragAnchor
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.common.ui.util.TransparentSystemBars
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.HiddenScreen
import com.perfomer.checkielite.navigation.voyager.impl.NavigatorHolder
import com.perfomer.checkielite.update.AppUpdateManager
import com.perfomer.checkielite.update.updateIfAvailable
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator as VoyagerBottomSheetNavigator

class AppActivity : AppCompatActivity() {

    private val updateManager: AppUpdateManager by inject()

    private val singleActivityHolder: SingleActivityHolder by inject()
    private val externalRouter: AndroidExternalRouter by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private val mainScreenProvider: MainScreenProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        singleActivityHolder.activity = this
        initExternalRouter()

        enableEdgeToEdge()

        setContent {
            TransparentSystemBars()

            CheckieLiteTheme {
                UsualNavigator()
                BottomSheetNavigator()
                OverlayNavigator()
                NavBarScrim()
            }

            ClearFocusOnKeyboardClose()
        }

        checkForUpdates()
    }

    @Composable
    private fun UsualNavigator() {
        Navigator(
            screen = mainScreenProvider() as Screen,
            onBackPressed = { false },
        ) { navigator ->
            navigatorHolder.usualNavigator = navigator

            SlideTransition(navigator = navigator) { screen ->
                screen.Content()
            }
        }
    }

    @Composable
    private fun BottomSheetNavigator() {
        VoyagerBottomSheetNavigator(
            sheetElevation = 24.dp,
            sheetBackgroundColor = LocalCuiPalette.current.BackgroundPrimary,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            content = { navigator ->
                navigatorHolder.bottomSheetNavigator = navigator
            },
            sheetContent = {
                CuiDragAnchor()
                CurrentScreen()
            },
            modifier = Modifier.imePadding()
        )
    }

    @Composable
    private fun OverlayNavigator() {
        Navigator(screen = HiddenScreen) { navigator ->
            navigatorHolder.overlayNavigator = navigator

            FadeTransition(navigator = navigator) { screen ->
                screen.Content()
            }
        }
    }

    /**
     * Init external router to register ActivityResultLauncher, it should be done on onCreate
     */
    private fun initExternalRouter() {
        externalRouter.register()
    }

    private fun checkForUpdates() = lifecycleScope.launch {
        updateManager.updateIfAvailable()
    }
}