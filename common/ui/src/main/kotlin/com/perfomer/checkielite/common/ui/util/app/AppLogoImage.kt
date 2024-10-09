package com.perfomer.checkielite.common.ui.util.app

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.util.isDebug

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    val iconResource = if (isDebug()) R.drawable.logo_brand_lite else R.drawable.logo_brand

    Image(
        painter = painterResource(id = iconResource),
        contentDescription = null,
        modifier = modifier,
    )
}