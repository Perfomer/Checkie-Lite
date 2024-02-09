package com.perfomer.checkielite.feature.reviewcreation.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import java.io.File

internal class ReviewCreationRepositoryImpl(
    private val applicationContext: Context,
    private val localDataSource: CheckieLocalDataSource,
) : ReviewCreationRepository {

    override suspend fun createReview(review: CheckieReview) {
        val picturesUri = review.picturesUri.map { uri ->
            val sourceFile = File(uri)
            val destinationFile = File(applicationContext.filesDir, sourceFile.nameWithoutExtension + ".webp")

            Compressor.compress(applicationContext, sourceFile) {
                format(Bitmap.CompressFormat.WEBP)
                destination(destinationFile)
                size(COMPRESS_TARGET_SIZE)
            }

            destinationFile.absolutePath
        }

        localDataSource.createReview(
            review = review.copy(
                picturesUri = picturesUri,
            )
        )
    }

    private companion object {
        const val COMPRESS_TARGET_SIZE = 1 * 1024 * 1024L // 1 MB in bytes
    }
}