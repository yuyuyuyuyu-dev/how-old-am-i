package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import dev.yuyuyuyuyu.howoldami.di.AppComponent
import dev.yuyuyuyuyu.howoldami.di.create

@Composable
fun MainScreen() {
    val component = remember { AppComponent::class.create() }

    val backStack: MutableList<MainNavigationRoute> =
        rememberSerializable(serializer = SnapshotStateListSerializer()) {
            mutableStateListOf(MainNavigationRoute.HowOldAmI)
        }

    MainNavigation(backStack, component)
}
