package dev.yuyuyuyuyu.howoldami

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.yuyuyuyuyu.howoldami.ui.HowOldAmIApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "how-old-am-i",
    ) {
        HowOldAmIApp()
    }
}
