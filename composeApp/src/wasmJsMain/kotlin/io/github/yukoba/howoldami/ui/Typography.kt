package io.github.yukoba.howoldami.ui

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import how_old_am_i.composeapp.generated.resources.Res
import how_old_am_i.composeapp.generated.resources.Yomogi_Regular
import org.jetbrains.compose.resources.Font

@Composable
fun typography() = Typography(
    defaultFontFamily = FontFamily(Font(Res.font.Yomogi_Regular))
)