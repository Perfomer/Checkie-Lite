package com.perfomer.checkielite.core.data.util

import android.content.Context
import android.net.Uri
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

private const val OUTPUT_STREAM_SIZE = 8192

internal fun archive(files: List<File>, destination: File) {
    destination.zipOutputStream().use { zipOutputStream ->
        files.forEach { file ->
            zipOutputStream.putNextEntry(ZipEntry(file.name))
            file.bufferedInputStream().use { bufferedInputStream ->
                bufferedInputStream.copyTo(zipOutputStream)
            }
        }
    }
}

internal fun unarchive(
    context: Context,
    zipFile: Uri,
    destinationResolver: (name: String) -> File,
) {
    zipFile.zipInputStream(context).use { zipInputStream ->
        var currentEntry = zipInputStream.nextEntry

        while (currentEntry!= null) {
            val destinationFile = destinationResolver(currentEntry.name)

            if (currentEntry.isDirectory) {
                destinationFile.mkdirs()
            } else {
                destinationFile.parentFile?.mkdirs()
                zipInputStream.copyTo(destinationFile.bufferedOutputStream())
            }

            currentEntry = zipInputStream.nextEntry
        }
    }
}

private fun File.bufferedOutputStream(size: Int = OUTPUT_STREAM_SIZE) = BufferedOutputStream(outputStream(), size)
private fun File.zipOutputStream(size: Int = OUTPUT_STREAM_SIZE) = ZipOutputStream(bufferedOutputStream(size))
private fun File.bufferedInputStream(size: Int = OUTPUT_STREAM_SIZE) = BufferedInputStream(inputStream(), size)

private fun Uri.bufferedInputStream(context: Context, size: Int = OUTPUT_STREAM_SIZE) = context.contentResolver.openInputStream(this)!!.buffered(size)
private fun Uri.zipInputStream(context: Context, size: Int = OUTPUT_STREAM_SIZE) = ZipInputStream(bufferedInputStream(context, size))
