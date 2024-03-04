package com.perfomer.checkielite.core.data.datasource.file

import android.content.Context
import android.graphics.Bitmap
import com.perfomer.checkielite.common.pure.util.randomUuid
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal interface FileDataSource {

    suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long = COMPRESS_TARGET_SIZE,
    ): String

    suspend fun deleteFile(uri: String)

    private companion object {
        const val COMPRESS_TARGET_SIZE = 1 * 1024 * 1024L // 1 MB in bytes
    }
}

internal class FileDataSourceImpl(
    private val applicationContext: Context,
) : FileDataSource {

    @Suppress("DEPRECATION")
    override suspend fun cacheCompressedPicture(
        uri: String,
        compressTargetSizeBytes: Long,
    ): String {
        val sourceFile = File(uri)
        val destinationFile = File(applicationContext.filesDir, randomUuid() + ".webp")

        Compressor.compress(applicationContext, sourceFile) {
            format(Bitmap.CompressFormat.WEBP)
            destination(destinationFile)
            size(compressTargetSizeBytes)
        }

        return destinationFile.absolutePath
    }

    override suspend fun deleteFile(uri: String): Unit = withContext(Dispatchers.IO) {
        File(uri).delete()
    }

}