package dev.yuyuyuyuyu.howoldami.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.yuyuyuyuyu.howoldami.di.howOldAmIAppModule
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen
import dev.yuyuyuyuyu.howoldami.ui.openSourceLicenseList.OpenSourceLicenseListScreen
import dev.yuyuyuyuyu.mymaterialtheme.MyMaterialTheme
import dev.yuyuyuyuyu.simpleTopAppBar.SimpleTopAppBar
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview
fun HowOldAmIApp() {
    val backStack = rememberSaveableBackStack(root = HowOldAmIScreen)
    val navigator = rememberCircuitNavigator(backStack) {}

    val focusManager = LocalFocusManager.current
    val uriHandler = LocalUriHandler.current

    KoinApplication(
        application = {
            printLogger()
            modules(howOldAmIAppModule)
        },
    ) {
        MyMaterialTheme {
            Scaffold(
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { focusManager.clearFocus() },
                ),
                topBar = {
                    SimpleTopAppBar(
                        title = "how-old-am-i",
                        navigateBackIsPossible = backStack.size > 1,
                        onNavigateBackButtonClick = { navigator.pop() },
                        onOpenSourceLicensesButtonClick = {
                            navigator.goTo(OpenSourceLicenseListScreen)
                        },
                        onSourceCodeButtonClick = {
                            uriHandler.openUri("https://github.com/yuyuyuyuyu-dev/how-old-am-i")
                        },
                    )
                },
            ) { innerPadding ->
                CircuitCompositionLocals(circuit = koinInject()) {
                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backStack,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
