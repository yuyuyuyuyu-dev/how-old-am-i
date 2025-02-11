package dev.yuyuyuyuyu.howoldami.ui.opensourcelicenses.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.yuyuyuyuyu.howoldami.ui.components.LibrariesContainer
import dev.yuyuyuyuyu.howoldami.ui.components.rememberLibraries
import how_old_am_i.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OpenSourceLicensesScreen(
    modifier: Modifier = Modifier,
) {
    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        showDescription = true,
    )
}
