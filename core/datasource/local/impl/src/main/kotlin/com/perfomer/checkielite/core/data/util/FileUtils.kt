package com.perfomer.checkielite.core.data.util

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private const val OUTPUT_STREAM_SIZE = 8192

internal fun archive(files: List<File>, destination: File) {
    destination.zipOutputStream().use { zipOs ->
        files.forEach { file ->
            zipOs.putNextEntry(ZipEntry(file.name))
            file.bufferedInputStream().use { bIs -> bIs.copyTo(zipOs) }
        }
    }
}

private fun File.bufferedOutputStream(size: Int = OUTPUT_STREAM_SIZE) = BufferedOutputStream(outputStream(), size)
private fun File.zipOutputStream(size: Int = OUTPUT_STREAM_SIZE) = ZipOutputStream(bufferedOutputStream(size))
private fun File.bufferedInputStream(size: Int = OUTPUT_STREAM_SIZE) = BufferedInputStream(inputStream(), size)

fun main() {
    val files = listOf(
        File("/Users/xor/Downloads/Ghibli/kaguyahime006.jpg"),
        File("/Users/xor/Downloads/Ghibli/kaguyahime035.jpg")
    )

    val destination = File("/Users/xor/work/kotlin/scratchpad-kotlin-java/src/main/kotlin/main/archive.zip")

    archive(files, destination)
}