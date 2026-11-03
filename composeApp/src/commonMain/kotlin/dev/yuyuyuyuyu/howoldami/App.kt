package dev.yuyuyuyuyu.howoldami

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.yuyuyuyuyu.howoldami.ui.main.MainScreen
import dev.yuyuyuyuyu.mycomposables.MyMaterialTheme
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun App(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    MyMaterialTheme {
        MainScreen(clock = clock, timeZone = timeZone)
    }
}
