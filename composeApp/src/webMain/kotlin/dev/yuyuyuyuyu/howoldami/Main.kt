package dev.yuyuyuyuyu.howoldami

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import howoldami.composeapp.generated.resources.Res
import howoldami.composeapp.generated.resources.language_tag
import kotlinx.browser.document
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        WebApp()
    }
}

@Composable
internal fun WebApp() {
    LaunchedEffect(Unit) {
        document.documentElement?.setAttribute("lang", getString(Res.string.language_tag))
    }
    App()
}
