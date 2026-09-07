package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.presentation.transition.ReviewSharedElement
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationElement

@Composable
internal fun ReviewDetailsHeader(
    productName: Text,
    brandName: Text?,
) {
    SelectionContainer {
        Column {
            if (brandName != null) {
                Text(
                    text = text(brandName),
                    color = LocalCuiPalette.current.TextAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .sharedNavigationElement(ReviewSharedElement.Subtitle)
                )

                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = text(productName),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .sharedNavigationElement(ReviewSharedElement.Title)
            )
        }
    }
}
