package com.perfomer.checkielite.core.data.datasource

import android.content.Context
import android.icu.util.Currency
import android.os.Environment
import androidx.core.content.ContextCompat
import com.perfomer.checkielite.common.pure.util.forEachAsync
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import com.perfomer.checkielite.core.entity.price.CurrencySymbol
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date
import java.util.Locale

internal class CheckieLocalDataSourceImpl(
    private val context: Context,
    private val databaseDataSource: DatabaseDataSource,
    private val fileDataSource: FileDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : CheckieLocalDataSource {

    @Volatile
    private var allCurrenciesCodes: List<String>? = null
    private val mutex = Mutex()

    override fun getReviews(): Flow<List<CheckieReview>> {
        return databaseDataSource.getReviews()
    }

    override fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>> {
        return databaseDataSource.getReviewsByBrand(brand)
    }

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return databaseDataSource.getReview(reviewId)
    }

    override fun getRecentSearches(): Flow<List<CheckieReview>> {
        return databaseDataSource.getRecentSearches()
    }

    override suspend fun rememberRecentSearch(reviewId: String) {
        return databaseDataSource.rememberRecentSearch(
            reviewId = reviewId,
            searchDate = Date(),
        )
    }

    override suspend fun clearRecentSearches() {
        return databaseDataSource.clearRecentSearches()
    }

    override suspend fun getAllBrands(): List<String> {
        return databaseDataSource.getAllBrands()
    }

    override suspend fun createReview(
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?
    ) {
        val actualPictures = pictures.map { picture -> picture.copy(id = randomUuid()) }
        val creationDate = Date()

        val isNeedSync = actualPictures.isNotEmpty()
        val reviewId = randomUuid()

        databaseDataSource.createReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            price = price,
            rating = rating,
            comment = comment,
            advantages = advantages,
            disadvantages = disadvantages,
            pictures = actualPictures,
            tagsIds = tagsIds,
            creationDate = creationDate,
            modificationDate = creationDate,
            isSyncing = isNeedSync,
        )

        if (price != null) {
            preferencesDataSource.setLatestCurrency(price.currency)
        }

        if (isNeedSync) {
            ContextCompat.startForegroundService(context, CompressorService.createIntent(context, reviewId, actualPictures.toArrayList()))
        }
    }

    override suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?
    ) = coroutineScope {
        val initialReview = getReview(reviewId).first()
        val initialPictures = initialReview.pictures

        val deletedPictures = initialPictures.filterNot(pictures::contains)
        deletedPictures.forEachAsync { picture -> fileDataSource.deleteFile(picture.uri) }

        val addedPictures = pictures.filter { picture -> picture.id == CheckiePicture.NO_ID }
        val actualAddedPictures = addedPictures.map { picture -> picture.copy(id = randomUuid()) }

        val actualPictures = mutableListOf<CheckiePicture>()

        pictures.forEach { picture ->
            val isPictureDeleted = deletedPictures.contains(picture)
            if (isPictureDeleted) return@forEach

            val addedPictureIndex = addedPictures.indexOf(picture)
            val isPictureAdded = addedPictureIndex != -1

            actualPictures +=
                if (isPictureAdded) actualAddedPictures[addedPictureIndex]
                else picture
        }

        val isNeedSync = addedPictures.isNotEmpty()

        databaseDataSource.updateReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            price = price,
            rating = rating,
            deletedPictures = deletedPictures,
            actualPictures = actualPictures,
            comment = comment,
            advantages = advantages,
            disadvantages = disadvantages,
            tagsIds = tagsIds,
            creationDate = initialReview.creationDate,
            modificationDate = Date(),
            isSyncing = isNeedSync,
        )

        if (price != null && initialReview.price?.currency != price.currency) {
            preferencesDataSource.setLatestCurrency(price.currency)
        }

        if (isNeedSync) {
            ContextCompat.startForegroundService(context, CompressorService.createIntent(context, reviewId, actualAddedPictures.toArrayList()))
        }
    }

    override suspend fun deleteReview(reviewId: String) {
        val review = getReview(reviewId).first()
        review.pictures.forEachAsync { picture ->
            fileDataSource.deleteFile(picture.uri)
        }
        databaseDataSource.deleteReview(reviewId, review.pictures)
    }

    override suspend fun dropSyncing() {
        databaseDataSource.dropSyncing()
    }

    override fun getTags(searchQuery: String, maxCount: Int): Flow<List<CheckieTag>> {
        return databaseDataSource.getTags(searchQuery, maxCount)
    }

    override suspend fun getTag(id: String): CheckieTag {
        return databaseDataSource.getTag(id)
    }

    override suspend fun getTagByName(name: String): CheckieTag? {
        return databaseDataSource.getTagByName(name)
    }

    override suspend fun createTag(value: String, emoji: String?): CheckieTag {
        val tag = CheckieTag(id = randomUuid(), value = value, emoji = emoji)
        databaseDataSource.createTag(tag)

        return tag
    }

    override suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag {
        val tag = CheckieTag(id = id, value = value, emoji = emoji)
        databaseDataSource.updateTag(tag)

        return tag
    }

    override suspend fun deleteTag(id: String) {
        databaseDataSource.deleteTag(id)
    }

    override suspend fun getAllCurrenciesCodes(): List<String> = mutex.withLock {
        val now = Date()
        val usedCurrencies = databaseDataSource.getUsedCurrencies().map { it.code }
        val localCurrencies = Currency.getAvailableCurrencyCodes(Locale.getDefault(), now)

        if (allCurrenciesCodes == null) {
            allCurrenciesCodes = withContext(Dispatchers.IO) {
                Locale.getAvailableLocales()
                    .asSequence()
                    .flatMap { locale -> Currency.getAvailableCurrencyCodes(locale, now).orEmpty().toList() }
                    .distinct()
                    .map(Currency::getInstance)
                    .sortedWith(
                        compareBy(
                            { it.currencyCode in MOST_COMMON_CURRENCY_CODES },
                            { (CurrencySymbol.getSymbol(it.currencyCode) ?: it.symbol).length > 1 },
                            { it.displayName },
                        )
                    )
                    .map(Currency::getCurrencyCode)
                    .toList()
            }
        }

        return (usedCurrencies + localCurrencies + allCurrenciesCodes.orEmpty())
            .distinct()
    }

    override suspend fun getLatestCurrency(): CheckieCurrency? {
        return preferencesDataSource.getLatestCurrency()
    }

    override suspend fun getLatestTagSortingStrategy(): TagSortingStrategy? {
        return preferencesDataSource.getLatestTagSortingStrategy()
    }

    override suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy) {
        preferencesDataSource.setLatestTagSortingStrategy(strategy)
    }

    override suspend fun exportBackup() {
        val documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
        val targetFolderPath = "$documentsFolder/Checkie"

        File(targetFolderPath).mkdirs()

        fileDataSource.createBackup(
            databaseUri = databaseDataSource.getDatabaseSourcePath(),
            picturesUri = databaseDataSource.getAllPicturesUri(),
            destinationFolderUri = targetFolderPath,
        )
    }

    override suspend fun importBackup(path: String) {
        fileDataSource.importBackup(
            backupPath = path,
            databaseTargetUri = databaseDataSource.getDatabaseSourcePath(),
        )
    }

    private companion object {
        private val MOST_COMMON_CURRENCY_CODES = listOf("USD", "EUR")
    }
}