package com.perfomer.checkielite.core.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.perfomer.checkielite.core.data.datasource.R
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.domain.entity.backup.BackupProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.Serializable

internal class BackupService : Service() {

    private val backupRepository: BackupRepository by inject()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val params = intent?.getSerializableExtra(EXTRA_PARAMS) as? BackupParams

        if (params == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotificationChannel()

        val notification = createForegroundNotification(params)
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
        ServiceCompat.startForeground(this, NOTIFICATION_ID_FOREGROUND, notification, foregroundServiceType)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        scope.launch {
            backupRepository.observeBackupState()
                .filterIsInstance<BackupProgress.InProgress>()
                .collect { progress ->
                    val updatedNotification = createForegroundNotification(params, progress.progress)
                    notificationManager.notify(NOTIFICATION_ID_FOREGROUND, updatedNotification)
                }
        }

        scope.launch {
            doWork(params)

            val successNotification = createSuccessNotification(params)
            notificationManager.notify(NOTIFICATION_ID_SUCCESS, successNotification)

            stopSelf()
        }

        return START_NOT_STICKY
    }

    private suspend fun doWork(mode: BackupParams) {
        when (mode) {
            is BackupParams.Export -> backupRepository.exportBackup()
            is BackupParams.Import -> backupRepository.importBackup(mode.path)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onTimeout(startId: Int, fgsType: Int) {
        super.onTimeout(startId, fgsType)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createForegroundNotification(mode: BackupParams, progress: Float = 0F): Notification {
        val progressPercent = (progress * 100).toInt()
        val titleRes = when (mode) {
            is BackupParams.Export -> R.string.notification_backup_export_progress_title
            is BackupParams.Import -> R.string.notification_backup_import_progress_title
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(titleRes))
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(progressPercent, 100, false)
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_logo_brand)
            .build()
    }

    private fun createSuccessNotification(mode: BackupParams): Notification {
        val titleRes = when (mode) {
            is BackupParams.Export -> R.string.notification_backup_export_success_title
            is BackupParams.Import -> R.string.notification_backup_import_success_title
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(titleRes))
            .setSmallIcon(R.drawable.ic_logo_brand)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_common),
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    companion object {
        private const val NOTIFICATION_ID_FOREGROUND = 112
        private const val NOTIFICATION_ID_SUCCESS = 113
        private const val CHANNEL_ID = "common"
        private const val EXTRA_PARAMS = "params"

        fun createIntent(context: Context, params: BackupParams): Intent {
            return Intent(context, BackupService::class.java)
                .putExtra(EXTRA_PARAMS, params)
        }
    }
}

internal sealed interface BackupParams : Serializable {

    data object Export : BackupParams {
        private fun readResolve(): Any = Export
    }

    data class Import(val path: String) : BackupParams
}