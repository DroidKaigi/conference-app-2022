package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched2022.core.designsystem.R.font

val fonts = FontFamily(
    Font(font.montserrat_variable_font_wght)
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = fonts,
        fontWeight = FontWeight.W600,
        fontStyle = FontStyle.Normal,
        lineHeight = 24.sp,
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 40.sp,
    ),
    /*
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    */
    /* Other default text styles to override
      button = TextStyle(
          fontFamily = FontFamily.Default,
          fontWeight = FontWeight.W500,
          fontSize = 14.sp
      ),
      caption = TextStyle(
          fontFamily = FontFamily.Default,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp
      )
      */
)
