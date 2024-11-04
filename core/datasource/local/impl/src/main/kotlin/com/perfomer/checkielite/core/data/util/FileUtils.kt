package com.perfomer.checkielite.core.data.util

import android.content.Context
import android.net.Uri
import com.perfomer.checkielite.core.data.datasource.file.backup.metadata.BackupMetadataParser.Companion.METADATA_FILENAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

private const val MODE_WRITE = "w"
private const val MODE_READ = "r"

fun File.deleteRecursivelyIf(condition: (File) -> Boolean): Boolean {
    return walkBottomUp()
        .fold(true) { res, it ->
            val isDeleted = if (condition(it)) it.delete() else true
            (isDeleted || !it.exists()) && res
        }
}

internal fun archive(
    files: List<File>,
    destination: File,
    metadata: String?,
): Flow<Float> = flow {
    var totalBytes = 0L
    var completedBytes = 0L

    files.forEach { file ->
        totalBytes += file.length()
    }

    destination.zipOutputStream().use { zipOutputStream ->
        files.forEach { file ->
            zipOutputStream.putNextEntry(ZipEntry(file.name))
            file.bufferedInputStream().use { bufferedInputStream ->
                completedBytes += bufferedInputStream.copyTo(zipOutputStream)
            }

            emit(completedBytes.toFloat() / totalBytes)
        }

        if (metadata != null) {
            zipOutputStream.putNextEntry(ZipEntry(METADATA_FILENAME))
            zipOutputStream.write(metadata.encodeToByteArray())
            zipOutputStream.closeEntry()
        }
    }
}
    .map { progress -> progress.coerceIn(0F, 1F) }
    .onStart { emit(0F) }
    .onCompletion { emit(1F) }
    .distinctUntilChanged()

internal fun unarchive(
    context: Context,
    zipFile: Uri,
    destinationResolver: (name: String) -> File?,
    metadataParser: (String) -> Unit,
): Flow<Float> = flow {
    var totalBytes = 0L
    var completedBytes = 0L

    zipFile.fileDescriptor(context, MODE_READ).use { descriptor ->
        totalBytes = descriptor?.statSize ?: throw IOException("Unable to get file descriptor")

        descriptor.fileDescriptor.zipInputStream().use { zipInputStream ->
            var currentEntry = zipInputStream.nextEntry

            while (currentEntry != null) {
                if (currentEntry.name == METADATA_FILENAME) {
                    val reader = zipInputStream.bufferedReader()
                    metadataParser(reader.readText())

                    currentEntry = zipInputStream.nextEntry
                    continue
                }

                val destinationFile = destinationResolver(currentEntry.name)

                if (destinationFile == null) {
                    currentEntry = zipInputStream.nextEntry
                    continue
                }

                if (currentEntry.isDirectory) {
                    destinationFile.mkdirs()
                } else {
                    destinationFile.parentFile?.mkdirs()
                    destinationFile.bufferedOutputStream().use { outStream ->
                        completedBytes += zipInputStream.copyTo(outStream)
                    }

                    emit(completedBytes.toFloat() / totalBytes)
                }

                currentEntry = zipInputStream.nextEntry
            }
        }
    }
}
    .map { progress -> progress.coerceIn(0F, 1F) }
    .onStart { emit(0F) }
    .onCompletion { emit(1F) }
    .distinctUntilChanged()

private fun File.bufferedOutputStream(size: Int = DEFAULT_BUFFER_SIZE) = BufferedOutputStream(outputStream(), size)
private fun File.zipOutputStream(size: Int = DEFAULT_BUFFER_SIZE) = ZipOutputStream(bufferedOutputStream(size))
private fun File.bufferedInputStream(size: Int = DEFAULT_BUFFER_SIZE) = BufferedInputStream(inputStream(), size)

private fun FileDescriptor.inputStream() = FileInputStream(this)
private fun FileDescriptor.bufferedInputStream(size: Int = DEFAULT_BUFFER_SIZE) = BufferedInputStream(inputStream(), size)
private fun FileDescriptor.zipInputStream(size: Int = DEFAULT_BUFFER_SIZE) = ZipInputStream(bufferedInputStream(size))

private fun Uri.fileDescriptor(context: Context, mode: String) = context.contentResolver.openFileDescriptor(this, mode)