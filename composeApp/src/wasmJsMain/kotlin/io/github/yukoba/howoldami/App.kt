package io.github.yukoba.howoldami

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.yukoba.howoldami.ui.typography
import io.github.yukoba.howoldami.ui.views.mainview.MainView

@Composable
fun App() {
    MaterialTheme(
        typography = typography()
    ) {
        MainView()
    }
}