package com.perfomer.checkielite.common.ui.theme

import androidx.compose.ui.graphics.Color


@Suppress("PropertyName")
interface CuiPalette {

    val TextPrimary: Color
    val TextSecondary: Color
    val TextAccent: Color
    val TextPositive: Color
    val TextNegative: Color

    val BackgroundPrimary: Color
    val BackgroundSecondary: Color
    val BackgroundAccentPrimary: Color
    val BackgroundAccentSecondary: Color
    val BackgroundPositive: Color
    val BackgroundNegative: Color

    val OutlinePrimary: Color get() = BackgroundSecondary
    val OutlineSecondary: Color
    val OutlineAccent: Color get() = BackgroundAccentPrimary

    val IconPrimary: Color
    val IconSecondary: Color
    val IconAccent: Color

    object Light : CuiPalette {

        override val TextPrimary: Color = CuiColorToken.Black
        override val TextSecondary: Color = CuiColorToken.GreyDark
        override val TextAccent: Color = CuiColorToken.OrangeDark
        override val TextPositive: Color = CuiColorToken.Green
        override val TextNegative: Color = CuiColorToken.Red

        override val BackgroundPrimary: Color = CuiColorToken.White
        override val BackgroundSecondary: Color = CuiColorToken.GreyLight
        override val BackgroundAccentPrimary: Color = CuiColorToken.Orange
        override val BackgroundAccentSecondary: Color = CuiColorToken.OrangeLight
        override val BackgroundPositive: Color = CuiColorToken.Green
        override val BackgroundNegative: Color = CuiColorToken.Red

        override val OutlineSecondary: Color = CuiColorToken.GreyLight2

        override val IconPrimary: Color = CuiColorToken.Black
        override val IconSecondary: Color = CuiColorToken.Grey
        override val IconAccent: Color = CuiColorToken.OrangeDark
    }
}

object CuiColorToken {
    val Orange = Color(0xFFF17A54)
    val OrangeDark = Color(0xFFDB4819)
    val OrangeLight = Color(0xFFFEE5DB)

    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF222B39)

    val GreyLight = Color(0xFFEDEDED)
    val GreyLight2 = Color(0xFFE6E6E6)
    val Grey = Color(0xFFC1C0CC)
    val GreyOrange = Color(0xFFB6A6A2)
    val GreyDark = Color(0xFF656160)

    val Green = Color(0xFF59B130)
    val Red = Color(0xFFDA3224)
}