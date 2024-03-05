package com.perfomer.checkielite.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalCuiPalette = compositionLocalOf<CuiPalette> { error("Not provided") }

@Suppress("PropertyName")
@Immutable
interface CuiPalette {

    val TextPrimary: Color
    val TextSecondary: Color
    val TextAccent: Color
    val TextPositive: Color
    val TextNegative: Color

    val BackgroundPrimary: Color
    val BackgroundSecondary: Color
    val BackgroundTertiary: Color
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

        override val TextPrimary: Color = CuiColorToken.Black1
        override val TextSecondary: Color = CuiColorToken.Grey5
        override val TextAccent: Color = CuiColorToken.Orange2
        override val TextPositive: Color = CuiColorToken.Green
        override val TextNegative: Color = CuiColorToken.Red

        override val BackgroundPrimary: Color = CuiColorToken.White1
        override val BackgroundSecondary: Color = CuiColorToken.Grey1
        override val BackgroundTertiary: Color = CuiColorToken.Grey4
        override val BackgroundAccentPrimary: Color = CuiColorToken.Orange1
        override val BackgroundAccentSecondary: Color = CuiColorToken.Orange3
        override val BackgroundPositive: Color = CuiColorToken.Green
        override val BackgroundNegative: Color = CuiColorToken.Red

        override val OutlineSecondary: Color = CuiColorToken.Grey2

        override val IconPrimary: Color = CuiColorToken.Black1
        override val IconSecondary: Color = CuiColorToken.Grey3
        override val IconAccent: Color = CuiColorToken.Orange1
    }

    object Dark : CuiPalette {

        override val TextPrimary: Color = CuiColorToken.White2
        override val TextSecondary: Color = CuiColorToken.Brown1
        override val TextAccent: Color = CuiColorToken.Orange1
        override val TextPositive: Color = CuiColorToken.Green
        override val TextNegative: Color = CuiColorToken.Red

        override val BackgroundPrimary: Color = CuiColorToken.Black2
        override val BackgroundSecondary: Color = CuiColorToken.Brown2
        override val BackgroundTertiary: Color = CuiColorToken.Brown5
        override val BackgroundAccentPrimary: Color = CuiColorToken.Orange1
        override val BackgroundAccentSecondary: Color = CuiColorToken.Brown3
        override val BackgroundPositive: Color = CuiColorToken.Green
        override val BackgroundNegative: Color = CuiColorToken.Red

        override val OutlineSecondary: Color = CuiColorToken.Brown4

        override val IconPrimary: Color = CuiColorToken.White2
        override val IconSecondary: Color = CuiColorToken.Brown1
        override val IconAccent: Color = CuiColorToken.Orange1
    }
}

object CuiColorToken {
    val Orange1 = Color(0xFFF17A54)
    val Orange2 = Color(0xFFDB4819)
    val Orange3 = Color(0xFFFEE5DB)

    val White1 = Color(0xFFFFFFFF)
    val White2 = Color(0xFFE4E9F1)

    val Black1 = Color(0xFF222B39)
    val Black2 = Color(0xFF1E1A19)

    val Grey1 = Color(0xFFF5F5F5)
    val Grey2 = Color(0xFFE6E6E6)
    val Grey3 = Color(0xFFC1C0CC)
    val Grey4 = Color(0xFFB6A6A2)
    val Grey5 = Color(0xFF656160)

    val Brown1 = Color(0xFFB2A095)
    val Brown2 = Color(0xFF2F2A2A)
    val Brown3 = Color(0xFF4E372E)
    val Brown4 = Color(0xFF383838)
    val Brown5 = Color(0xFF6D5852)

    val Green = Color(0xFF59B130)
    val Red = Color(0xFFDA3224)
}