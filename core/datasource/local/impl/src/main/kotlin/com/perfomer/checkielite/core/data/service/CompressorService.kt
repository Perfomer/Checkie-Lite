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
import com.perfomer.checkielite.common.pure.util.mapAsync
import com.perfomer.checkielite.core.data.datasource.R
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.PictureSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

internal class CompressorService : Service() {

    private val fileDataSource: FileDataSource by inject()
    private val databaseDataSource: DatabaseDataSource by inject()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reviewId = intent?.getStringExtra(EXTRA_REVIEW_ID)
        val picturesUri = intent?.getSerializableExtra(EXTRA_PICTURES_URI) as? ArrayList<CheckiePicture>

        if (reviewId.isNullOrEmpty() || picturesUri.isNullOrEmpty()) {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotificationChannel()

        val notification = createForegroundNotification()
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, foregroundServiceType)

        scope.launch {
            doWork(reviewId, picturesUri)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private suspend fun doWork(
        reviewId: String,
        pictures: List<CheckiePicture>,
    ) {
        val compressedPictures = pictures.mapAsync { picture ->
            picture.copy(
                uri = fileDataSource.cacheCompressedPicture(picture.uri),
                source = PictureSource.APP,
            )
        }

        fileDataSource.clearCompressorCache()

        databaseDataSource.updatePictures(compressedPictures)
        databaseDataSource.updateSyncing(reviewId, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_compression_title))
            .setOngoing(true)
            .setSilent(true)
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
        private const val NOTIFICATION_ID = 111
        private const val CHANNEL_ID = "common"
        private const val EXTRA_REVIEW_ID = "reviewId"
        private const val EXTRA_PICTURES_URI = "pictures_uri"

        fun createIntent(
            context: Context,
            reviewId: String,
            picturesUri: ArrayList<CheckiePicture>,
        ): Intent {
            return Intent(context, CompressorService::class.java)
                .putExtra(EXTRA_REVIEW_ID, reviewId)
                .putExtra(EXTRA_PICTURES_URI, picturesUri)
        }
    }
}