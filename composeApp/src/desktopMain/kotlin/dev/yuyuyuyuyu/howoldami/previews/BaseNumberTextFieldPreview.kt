package dev.yuyuyuyuyu.howoldami.previews

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import dev.yuyuyuyuyu.howoldami.ui.components.BaseNumberTextField

@Preview
@Composable
fun BaseNumberTextFieldPreview() {
    BaseNumberTextField(
        value = "149",
        onValueChange = {
            println("new value: $it")
        },
    )
}
