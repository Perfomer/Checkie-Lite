package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state

import androidx.compose.ui.util.fastMap
import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.domain.entity.price.CheckiePrice
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import kotlinx.collections.immutable.toPersistentList
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

internal class ReviewDetailsUiStateMapper : UiStateMapper<ReviewDetailsState, ReviewDetailsUiState> {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun map(state: ReviewDetailsState): ReviewDetailsUiState {
        return when (state.review) {
            is Lce.Content -> {
                val content = state.review.content
                val review = content.review

                ReviewDetailsUiState.Content(
                    brandName = review.productBrand?.uppercase(),
                    productName = review.productName,
                    date = dateFormat.format(review.creationDate),
                    rating = review.rating,
                    price = review.price?.toUi(),
                    picturesUri = review.pictures.fastMap { it.uri }.toPersistentList(),
                    currentPicturePosition = state.currentPicturePosition,
                    comment = review.comment,
                    advantages = review.advantages,
                    disadvantages = review.disadvantages,
                    isMenuAvailable = !review.isSyncing,
                    tags = review.tags.fastMap { it.toUi() }.toPersistentList(),
                    recommendations = content.recommendations.fastMap { it.toRecommendation() }.toPersistentList(),
                )
            }

            is Lce.Loading -> ReviewDetailsUiState.Loading
            is Lce.Error -> ReviewDetailsUiState.Error
        }
    }

    private fun CheckieTag.toUi(): Tag {
        return Tag(
            tagId = id,
            text = value,
            emoji = emoji,
        )
    }

    private fun CheckieReview.toRecommendation(): RecommendedReview {
        return RecommendedReview(
            reviewId = id,
            brandName = productBrand?.uppercase(),
            productName = productName,
            pictureUri = pictures.firstOrNull()?.uri,
            rating = rating,
            isSyncing = isSyncing,
        )
    }

    private fun CheckiePrice.toUi(): Price {
        val currency = Currency.getInstance(currency.code)
        val decimalSeparator = DecimalFormatSymbols.getInstance().monetaryDecimalSeparator
        val priceFormat = DecimalFormat.getCurrencyInstance().apply {
            this.currency = currency
            this.minimumFractionDigits = 0
        }

        var formattedPrice = priceFormat.format(value)

        val alternateSymbol = com.perfomer.checkielite.core.domain.entity.price.CurrencySymbol.getSymbol(currency.currencyCode)
        if (alternateSymbol != null) {
            formattedPrice = formattedPrice.replace(currency.symbol, alternateSymbol)
        }

        val fractionalPartStartIndex = formattedPrice.indexOf(decimalSeparator) + 1
        val fractionalPart = formattedPrice.substringAfter(
            delimiter = decimalSeparator,
            missingDelimiterValue = "",
        )
            .filter { it.isDigit() }
            .takeIf { it.isNotEmpty() }

        val fractionalPartIndices = fractionalPart?.let {
            IntRange(fractionalPartStartIndex, fractionalPartStartIndex + fractionalPart.length)
        }

        return Price(
            value = formattedPrice,
            fractionalPartIndices = fractionalPartIndices,
        )
    }
}