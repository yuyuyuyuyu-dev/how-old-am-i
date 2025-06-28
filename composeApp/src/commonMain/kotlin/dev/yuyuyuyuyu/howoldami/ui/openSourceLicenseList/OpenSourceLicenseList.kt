package dev.yuyuyuyuyu.howoldami.ui.openSourceLicenseList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import how_old_am_i.composeapp.generated.resources.Res
import kotlinx.collections.immutable.toImmutableList

@Composable
fun OpenSourceLicenseList(modifier: Modifier = Modifier) {
    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    LibrariesContainer(
        libraries = libraries?.libraries?.distinctBy { it.name }?.let {
            libraries?.copy(libraries = it.toImmutableList())
        },
        modifier = modifier,
        showDescription = true,
    )
}
