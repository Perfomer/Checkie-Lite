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
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
internal fun ReviewDetailsHeader(
    productName: String,
    brandName: String?,
) {
    SelectionContainer {
        Column {
            if (brandName != null) {
                Text(
                    text = brandName,
                    color = LocalCuiPalette.current.TextAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )

                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = productName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}