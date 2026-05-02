package dev.yuyuyuyuyu.howoldami.ui.openSourceLicenses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import howoldami.composeapp.generated.resources.Res

@Composable
fun OpenSourceLicensesScreen(modifier: Modifier = Modifier) {
    val libraries by produceLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    LibrariesContainer(
        libraries = libraries?.libraries?.distinctBy { it.name }?.let {
            libraries?.copy(libraries = it)
        },
        modifier = modifier,
        showDescription = true,
    )
}
