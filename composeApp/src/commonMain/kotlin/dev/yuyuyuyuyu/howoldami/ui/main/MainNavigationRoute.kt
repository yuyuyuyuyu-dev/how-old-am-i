package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavigationRoute : NavKey {
    @Serializable
    data object HowOldAmI : MainNavigationRoute

    @Serializable
    data object OpenSourceLicenses : MainNavigationRoute
}
