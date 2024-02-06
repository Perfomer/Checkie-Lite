package com.perfomer.checkielite.feature.reviewcreation.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import java.io.File

internal class ReviewCreationRepositoryImpl(
    private val applicationContext: Context,
) : ReviewCreationRepository {

    override suspend fun createReview(review: CheckieReview) {
        review.imagesUri.forEach { uri ->
            val sourceFile = File(uri)
            val destinationFile = File(applicationContext.filesDir, sourceFile.nameWithoutExtension + ".webp")

            Compressor.compress(applicationContext, sourceFile) {
                format(Bitmap.CompressFormat.WEBP)
                destination(destinationFile)
                size(COMPRESS_TARGET_SIZE)
            }
        }
    }

    private companion object {
        const val COMPRESS_TARGET_SIZE = 1 * 1024 * 1024L // 1 MB in bytes
    }
}