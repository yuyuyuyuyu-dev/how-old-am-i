package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import dev.yuyuyuyuyu.howoldami.di.AppComponent
import dev.yuyuyuyuyu.howoldami.di.create
import dev.yuyuyuyuyu.simpleTopAppBar.SimpleTopAppBar
import howoldami.composeapp.generated.resources.Res
import howoldami.composeapp.generated.resources.app_name
import howoldami.composeapp.generated.resources.open_source_licenses
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    val component = remember { AppComponent::class.create() }

    val backStack: MutableList<MainNavigationRoute> =
        rememberSerializable(serializer = SnapshotStateListSerializer()) {
            mutableStateListOf(MainNavigationRoute.HowOldAmI)
        }

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            SimpleTopAppBar(
                title =
                    when (backStack.lastOrNull()) {
                        is MainNavigationRoute.OpenSourceLicenses -> stringResource(Res.string.open_source_licenses)
                        else -> stringResource(Res.string.app_name)
                    },
                navigateBackIsPossible = backStack.size > 1,
                onNavigateBackButtonClick = { backStack.removeLastOrNull() },
                onOpenSourceLicensesButtonClick = {
                    if (backStack.lastOrNull() != MainNavigationRoute.OpenSourceLicenses) {
                        backStack.add(MainNavigationRoute.OpenSourceLicenses)
                    }
                },
                onSourceCodeButtonClick = {
                    uriHandler.openUri("https://github.com/yuyuyuyuyu-dev/how-old-am-i")
                },
            )
        },
    ) { innerPadding ->
        MainNavigation(backStack, component, Modifier.padding(innerPadding))
    }
}
