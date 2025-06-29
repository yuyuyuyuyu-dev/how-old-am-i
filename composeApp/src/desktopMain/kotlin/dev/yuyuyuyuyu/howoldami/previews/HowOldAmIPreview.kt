package dev.yuyuyuyuyu.howoldami.previews

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmI
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen

@Preview
@Composable
fun HowOldAmIPreview() {
    HowOldAmI(
        state = HowOldAmIScreen.State(age = "12") {}
    )
}
