package com.perfomer.checkielite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.TransparentSystemBars
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.StartStackScreenProvider
import org.koin.android.ext.android.inject

class AppActivity : AppCompatActivity() {

    private val singleActivityHolder: SingleActivityHolder by inject()
    private val startStackScreenProvider: StartStackScreenProvider by inject()
    private val externalRouter: AndroidExternalRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        singleActivityHolder.activity = this
        initExternalRouter()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TransparentSystemBars()

            CheckieLiteTheme {
                startStackScreenProvider.Content()
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