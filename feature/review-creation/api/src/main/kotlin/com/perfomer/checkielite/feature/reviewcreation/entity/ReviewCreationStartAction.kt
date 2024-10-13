package com.perfomer.checkielite.feature.reviewcreation.entity

enum class ReviewCreationStartAction(val targetPage: ReviewCreationPage) {
    NONE(ReviewCreationPage.entries.first()),
    SET_PRICE(ReviewCreationPage.PRODUCT_INFO),
    ADD_PICTURES(ReviewCreationPage.PRODUCT_INFO),
    ADD_TAGS(ReviewCreationPage.TAGS),
    ADD_REVIEW_COMMENT(ReviewCreationPage.REVIEW_INFO),
}