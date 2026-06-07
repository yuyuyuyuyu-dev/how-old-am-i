package dev.yuyuyuyuyu.howoldami

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "How Old Am I"
    ) {
        App()
    }
}
