package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.modifier.localSharedElement
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiFadedText
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun ReviewDetailsHeader(
    id: String,
    productName: String,
    brandName: String?,
) {
    SelectionContainer {
        Column {
            if (brandName != null) {
               CuiFadedText(
                    text = brandName,
                    color = LocalCuiPalette.current.TextAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .localSharedElement(id + brandName)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),

                )

                Spacer(Modifier.height(8.dp))
            }

            CuiFadedText(
                text = productName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .localSharedElement(id + productName)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            )
        }
    }
}