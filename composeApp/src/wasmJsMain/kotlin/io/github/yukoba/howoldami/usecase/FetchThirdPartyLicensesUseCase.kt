package io.github.yukoba.howoldami.usecase

import how_old_am_i.composeapp.generated.resources.Res
import io.github.yukoba.howoldami.data.types.Artifact
import io.github.yukoba.howoldami.ui.features.thirdpartylicenses.types.ThirdPartyLicense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
class FetchThirdPartyLicensesUseCase {
    operator suspend fun invoke(
        onSuccess: (List<ThirdPartyLicense>) -> Unit,
    ) = withContext(Dispatchers.Default) {
        val jsonText = Res.readBytes("files/artifacts.json").decodeToString()
        val artifacts: List<Artifact> = Json.decodeFromString(jsonText)
        val thirdPartyLicenses = artifacts.map { artifact ->
            ThirdPartyLicense(
                libraryName = artifact.name,
                website = artifact.scm.url,
                licenseName = artifact.spdxLicenses?.first()?.name ?: artifact.unknownLicenses?.first()?.name ?: "",
                licenseUrl = artifact.spdxLicenses?.first()?.url ?: artifact.unknownLicenses?.first()?.url ?: "",
            )
        }

        onSuccess(thirdPartyLicenses)
    }
}