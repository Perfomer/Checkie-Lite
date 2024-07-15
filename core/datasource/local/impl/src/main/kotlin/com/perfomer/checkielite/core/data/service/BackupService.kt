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
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File

internal class BackupService : Service() {

    private val fileDataSource: FileDataSource by inject()
    private val databaseDataSource: DatabaseDataSource by inject()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mode = intent?.getSerializableExtra(EXTRA_MODE) as? BackupMode
        val path = intent?.getStringExtra(EXTRA_PATH)

        if (mode == null || path.isNullOrBlank()) {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotificationChannel()

        val notification = createForegroundNotification(mode)
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, foregroundServiceType)

        scope.launch {
            doWork(mode, path)
        }

        return START_NOT_STICKY
    }

    private suspend fun doWork(mode: BackupMode, path: String) {
        when (mode) {
            BackupMode.EXPORT -> exportBackup(path)
            BackupMode.IMPORT -> importBackup(path)
        }

        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun exportBackup(path: String) {
        File(path).mkdirs()

        fileDataSource.exportBackup(
            databaseUri = databaseDataSource.getDatabaseSourcePath(),
            picturesUri = databaseDataSource.getAllPicturesUri(),
            destinationFolderUri = path,
        )
    }

    private suspend fun importBackup(path: String) {
        fileDataSource.importBackup(
            backupPath = path,
            databaseTargetUri = databaseDataSource.getDatabaseSourcePath(),
        )
    }

    private fun createForegroundNotification(mode: BackupMode): Notification {
        val titleRes = when (mode) {
            BackupMode.EXPORT -> R.string.notification_backup_export_title
            BackupMode.IMPORT -> R.string.notification_backup_import_title
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(titleRes))
            .setOngoing(true)
            .setProgress(1, 1, true)
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
        private const val NOTIFICATION_ID = 112
        private const val CHANNEL_ID = "common"
        private const val EXTRA_MODE = "mode"
        private const val EXTRA_PATH = "path"

        fun createIntent(context: Context, mode: BackupMode, path: String): Intent {
            return Intent(context, BackupService::class.java)
                .putExtra(EXTRA_MODE, mode)
                .putExtra(EXTRA_PATH, path)
        }
    }
}

internal enum class BackupMode {
    EXPORT, IMPORT
}