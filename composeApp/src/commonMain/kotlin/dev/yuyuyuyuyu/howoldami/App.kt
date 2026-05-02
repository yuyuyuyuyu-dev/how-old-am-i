package dev.yuyuyuyuyu.howoldami

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.yuyuyuyuyu.howoldami.ui.main.MainScreen
import dev.yuyuyuyuyu.mymaterialtheme.MyMaterialTheme

@Composable
@Preview
fun App() {
    MyMaterialTheme {
        MainScreen()
    }
}
