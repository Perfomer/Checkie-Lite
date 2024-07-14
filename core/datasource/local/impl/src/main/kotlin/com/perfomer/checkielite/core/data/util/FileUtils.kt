package com.perfomer.checkielite.core.data.util

import android.content.Context
import android.net.Uri
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

private const val MODE_WRITE = "w"
private const val MODE_READ = "r"

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
    zipFile.fileDescriptor(context, MODE_READ).use { descriptor ->
        descriptor?.fileDescriptor?.zipInputStream()?.use { zipInputStream ->
            var currentEntry = zipInputStream.nextEntry

            while (currentEntry != null) {
                val destinationFile = destinationResolver(currentEntry.name)

                if (currentEntry.isDirectory) {
                    destinationFile.mkdirs()
                } else {
                    destinationFile.parentFile?.mkdirs()
                    destinationFile.bufferedOutputStream().use { outStream ->
                        zipInputStream.copyTo(outStream)
                    }
                }

                currentEntry = zipInputStream.nextEntry
            }
        }
    }
}

private fun File.bufferedOutputStream(size: Int = DEFAULT_BUFFER_SIZE) = BufferedOutputStream(outputStream(), size)
private fun File.zipOutputStream(size: Int = DEFAULT_BUFFER_SIZE) = ZipOutputStream(bufferedOutputStream(size))
private fun File.bufferedInputStream(size: Int = DEFAULT_BUFFER_SIZE) = BufferedInputStream(inputStream(), size)

private fun FileDescriptor.inputStream() = FileInputStream(this)
private fun FileDescriptor.bufferedInputStream(size: Int = DEFAULT_BUFFER_SIZE) = BufferedInputStream(inputStream(), size)
private fun FileDescriptor.zipInputStream(size: Int = DEFAULT_BUFFER_SIZE) = ZipInputStream(bufferedInputStream(size))

private fun Uri.fileDescriptor(context: Context, mode: String) = context.contentResolver.openFileDescriptor(this, mode)