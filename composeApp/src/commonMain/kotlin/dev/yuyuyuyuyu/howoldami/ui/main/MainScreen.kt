package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer

@Composable
fun MainScreen() {
    val backStack: MutableList<MainNavigationRoute> =
        rememberSerializable(serializer = SnapshotStateListSerializer()) {
            mutableStateListOf(MainNavigationRoute.HowOldAmI)
        }

    MainNavigation(backStack)
}
