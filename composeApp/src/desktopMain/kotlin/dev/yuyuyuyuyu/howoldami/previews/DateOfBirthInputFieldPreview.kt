package dev.yuyuyuyuyu.howoldami.previews

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import dev.yuyuyuyuyu.howoldami.ui.components.DateOfBirthInputField
import dev.yuyuyuyuyu.howoldami.ui.models.DateOfBirth

@Preview
@Composable
fun DateOfBirthInputFieldPreview() {
    DateOfBirthInputField(
        value = DateOfBirth(year = "149", month = "07", day = "31"),
        onValueChange = {},
    )
}
