package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen
import dev.yuyuyuyuyu.howoldami.ui.openSourceLicenses.OpenSourceLicensesScreen

@Composable
fun MainNavigation(backStack: MutableList<MainNavigationRoute>, modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                MainNavigationRoute.HowOldAmI -> NavEntry(key) {
                    HowOldAmIScreen()
                }

                MainNavigationRoute.OpenSourceLicenses -> NavEntry(key) {
                    OpenSourceLicensesScreen()
                }
            }
        }
    )
}
