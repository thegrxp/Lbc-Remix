package com.ainrom.lbcremix.ui

import androidx.compose.runtime.*
import com.ainrom.lbcremix.ui.theme.LbcRemixTheme
import com.ainrom.lbcremix.ui.theme.colorPrimary
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LbcRemixApp() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(colorPrimary)
    }

    LbcRemixTheme {
        LbcRemixNavGraph()
    }
}