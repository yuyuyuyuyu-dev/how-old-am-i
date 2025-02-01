package io.github.yukoba.howoldami.ui.thirdpartylicenses.types

import io.github.yukoba.howoldami.data.types.ThirdPartyLicense

data class ThirdPartyLicensesUiState(
    val thirdPartyLicenses: List<ThirdPartyLicense> = emptyList(),
)
