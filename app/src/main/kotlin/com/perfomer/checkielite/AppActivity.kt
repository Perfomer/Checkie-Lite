package com.perfomer.checkielite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.android.apprestart.AppRestarter
import com.perfomer.checkielite.common.android.apprestart.RestartAction
import com.perfomer.checkielite.common.android.apprestart.RestartAction.ShowSuccessBackupImportToast
import com.perfomer.checkielite.common.android.permissions.PermissionHelper
import com.perfomer.checkielite.common.ui.cui.widget.scrim.NavBarScrim
import com.perfomer.checkielite.common.ui.cui.widget.toast.CuiToastHost
import com.perfomer.checkielite.common.ui.cui.widget.toast.CuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberSuccessToast
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.common.ui.util.TransparentSystemBars
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.common.update.api.updateIfAvailable
import com.perfomer.checkielite.core.navigation.NavigationHost
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.BackupNavigationManager
import com.perfomer.checkielite.navigation.StartScreenProvider
import com.perfomer.checkielite.newnavigation.ADestination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AppActivity : AppCompatActivity() {

    private val updateManager: AppUpdateManager by inject()
    private val singleActivityHolder: SingleActivityHolder by inject()
    private val externalRouter: AndroidExternalRouter by inject()
    private val permissionHelper: PermissionHelper by inject()
    private val backupNavigationManager: BackupNavigationManager by inject()
    private val startScreenProvider: StartScreenProvider by inject()
    private val appRestarter: AppRestarter by inject()
    private val navigationHost: NavigationHost by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val restartActions = appRestarter.extractStartActions().toImmutableList()

        initializeApplication()

        enableEdgeToEdge()

        setContent {
            TransparentSystemBars()

            CheckieLiteTheme {
                EnrichCompositionLocal {
                    navigationHost.Root()

                    NavBarScrim()
                    CuiToastHost()
                    RestartActionsHandler(restartActions)
                }
            }

            ClearFocusOnKeyboardClose()
        }

        checkForUpdates()
    }

    private fun initializeApplication() {
        singleActivityHolder.activity = this

        externalRouter.register()
        permissionHelper.register()
        backupNavigationManager.register(lifecycleScope)

        navigationHost.initialize(startDestination = ADestination)
    }

    @Composable
    private fun EnrichCompositionLocal(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalCuiToastHostState provides remember { CuiToastHostState() },
            content = content
        )
    }

    @Composable
    private fun RestartActionsHandler(restartActions: ImmutableList<RestartAction>) {
        val toastHost = LocalCuiToastHostState.current

        val backupImportSuccessToast = rememberSuccessToast(R.string.settings_backup_success_import)

        LaunchedEffect(Unit) {
            restartActions.forEach { restartAction ->
                when (restartAction) {
                    is ShowSuccessBackupImportToast -> {
                        delay(1_500L)
                        toastHost.showToast(backupImportSuccessToast)
                    }
                }
            }
        }
    }

    private fun checkForUpdates() = lifecycleScope.launch {
        updateManager.updateIfAvailable()
    }
}