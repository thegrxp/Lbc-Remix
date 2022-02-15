package com.ainrom.lbcremix.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LbcRemixThemeColors = lightColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryVariant,
    onPrimary = colorWhite,
    secondary = colorSecondary,
    secondaryVariant = colorSecondary,
    onSecondary = colorWhite,
    error = colorRed,
    onBackground = colorBlack,
)

@Composable
fun LbcRemixTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LbcRemixThemeColors,
        typography = LbcRemixTypography,
        shapes = LbcRemixShapes,
        content = content
    )
}
