package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import dev.yuyuyuyuyu.howoldami.di.AppComponent
import dev.yuyuyuyuyu.howoldami.di.create
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen
import dev.yuyuyuyuyu.mycomposables.MyScaffold
import howoldami.composeapp.generated.resources.Res
import howoldami.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    val component = remember { AppComponent::class.create() }

    val focusManager = LocalFocusManager.current
    val uriHandler = LocalUriHandler.current

    val libraries by produceLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    MyScaffold(
        title = stringResource(Res.string.app_name),
        libraries =
            libraries?.libraries?.distinctBy { it.name }?.let {
                libraries?.copy(libraries = it)
            },
        modifier =
            Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = { focusManager.clearFocus() }
            ),
        onSourceCodeButtonClick = {
            uriHandler.openUri("https://github.com/yuyuyuyuyu-dev/how-old-am-i")
        }
    ) { innerPadding ->
        HowOldAmIScreen(
            viewModel = component.howOldAmIViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
