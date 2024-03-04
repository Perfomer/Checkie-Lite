package com.perfomer.checkielite

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.HiddenScreen
import com.perfomer.checkielite.navigation.voyager.impl.NavigatorHolder
import org.koin.android.ext.android.inject

class AppActivity : AppCompatActivity() {

    private val singleActivityHolder: SingleActivityHolder by inject()
    private val externalRouter: AndroidExternalRouter by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private val mainScreenProvider: MainScreenProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        singleActivityHolder.activity = this
        initExternalRouter()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )

        setContent {
            CheckieLiteTheme {
                UsualNavigator()
                OverlayNavigator()
            }

            ClearFocusOnKeyboardClose()
        }
    }

    @Composable
    fun UsualNavigator() {
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
}