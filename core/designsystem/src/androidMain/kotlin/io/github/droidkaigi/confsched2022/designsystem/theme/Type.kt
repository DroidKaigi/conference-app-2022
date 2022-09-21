package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched2022.core.designsystem.R.font

val montserratFonts = FontFamily(
    Font(font.montserrat_medium)
)

val robotoFonts = FontFamily(
    Font(font.roboto_regular)
)

val robotoMediumFonts = FontFamily(
    Font(font.roboto_medium)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        fontFamily = montserratFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 64.sp,
    ),
    displayMedium = TextStyle(
        fontSize = 45.sp,
        fontFamily = montserratFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 52.sp,
    ),
    displaySmall = TextStyle(
        fontSize = 36.sp,
        fontFamily = montserratFonts,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        lineHeight = 44.sp,
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontFamily = montserratFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        fontFamily = robotoFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = robotoFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 32.sp,
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = montserratFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = montserratFonts,
        fontWeight = FontWeight.W600,
        fontStyle = FontStyle.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoMediumFonts,
        fontWeight = FontWeight.W600,
        fontStyle = FontStyle.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoMediumFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = robotoMediumFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        fontFamily = robotoMediumFonts,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = robotoFonts,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoFonts,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = robotoFonts,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
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

@Composable
@Preview
fun FontPreview() {
    Column(
        Modifier.background(color = Color.White)
    ) {
        Text("Display Large", style = Typography.displayLarge)
        Text("Display Medium", style = Typography.displayMedium)
        Text("Display Small", style = Typography.displaySmall)
        Text("Headline Large", style = Typography.headlineLarge)
        Text("Headline Medium", style = Typography.headlineMedium)
        Text("Headline Small", style = Typography.headlineSmall)
        Text("Title Large", style = Typography.titleLarge)
        Text("Title Medium", style = Typography.titleMedium)
        Text("Title Small", style = Typography.titleSmall)
        Text("Label Large", style = Typography.labelLarge)
        Text("Label Medium", style = Typography.labelMedium)
        Text("Label Small", style = Typography.labelSmall)
        Text("Body Large", style = Typography.bodyLarge)
        Text("Body Medium", style = Typography.bodyMedium)
        Text("Body Small", style = Typography.bodySmall)
    }
}
