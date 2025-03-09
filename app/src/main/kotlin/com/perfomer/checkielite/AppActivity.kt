package com.perfomer.checkielite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.perfomer.checkielite.common.android.SingleActivityHolder
import com.perfomer.checkielite.common.android.apprestart.AppRestarter
import com.perfomer.checkielite.common.android.apprestart.RestartAction
import com.perfomer.checkielite.common.android.apprestart.RestartAction.ShowSuccessBackupImportToast
import com.perfomer.checkielite.common.android.permissions.PermissionHelper
import com.perfomer.checkielite.common.ui.cui.widget.scrim.NavBarScrim
import com.perfomer.checkielite.common.ui.cui.widget.sheet.CuiDragAnchor
import com.perfomer.checkielite.common.ui.cui.widget.toast.CuiToastHost
import com.perfomer.checkielite.common.ui.cui.widget.toast.CuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberSuccessToast
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.ClearFocusOnKeyboardClose
import com.perfomer.checkielite.common.ui.util.TransparentSystemBars
import com.perfomer.checkielite.common.ui.util.navigation.DefaultBottomSheetDismissHandlerOwner
import com.perfomer.checkielite.common.ui.util.navigation.LocalBottomSheetDismissHandlerOwner
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.common.update.api.updateIfAvailable
import com.perfomer.checkielite.core.navigation.NavigationHost
import com.perfomer.checkielite.navigation.AndroidExternalRouter
import com.perfomer.checkielite.navigation.BackupNavigationManager
import com.perfomer.checkielite.navigation.ComposablesBottomSheetController
import com.perfomer.checkielite.navigation.ComposablesBottomSheetRoot
import com.perfomer.checkielite.navigation.StartScreenProvider
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
                    Content()

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

        navigationHost.initialize(startDestination = startScreenProvider())
    }

    @Composable
    private fun EnrichCompositionLocal(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalCuiToastHostState provides remember { CuiToastHostState() },
            LocalBottomSheetDismissHandlerOwner provides remember { DefaultBottomSheetDismissHandlerOwner() },
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

    @Composable
    private fun Content() = with(navigationHost) {
        val dismissHandlerOwner = LocalBottomSheetDismissHandlerOwner.current

        val sheetState = rememberModalBottomSheetState(
            initialDetent = SheetDetent.Hidden,
            positionalThreshold = { totalDistance -> (totalDistance / 4).coerceIn(128.dp, 384.dp) },
            velocityThreshold = { 24_576.dp },
            confirmDetentChange = { detent ->
                if (detent == SheetDetent.Hidden) {
                    dismissHandlerOwner.current.onDismissRequested()
                } else {
                    true
                }
            },
        )

        val bottomSheetController = remember { ComposablesBottomSheetController(sheetState) }

        Root(
            bottomSheetController = bottomSheetController,
            bottomSheetContent = { content ->
                ComposablesBottomSheetRoot(
                    sheetState = sheetState,
                    sheetElevation = LocalCuiPalette.current.LargeElevation,
                    containerColor = LocalCuiPalette.current.BackgroundPrimary,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    dragHandle = { CuiDragAnchor() },
                    onDismiss = ::dismissBottomSheet,
                    content = content,
                )
            }
        )
    }

    private fun checkForUpdates() = lifecycleScope.launch {
        updateManager.updateIfAvailable()
    }
}