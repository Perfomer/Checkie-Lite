package com.perfomer.checkielite.feature.reviewdetails.presentation.transition

import com.perfomer.checkielite.core.navigation.transition.SharedContentRole

data object ReviewListItem : SharedContentRole {
    override val group = ReviewContent
}

data object ReviewPage : SharedContentRole {
    override val group = ReviewContent
}

data object ReviewRecommendation : SharedContentRole {
    override val group = ReviewContent
}
