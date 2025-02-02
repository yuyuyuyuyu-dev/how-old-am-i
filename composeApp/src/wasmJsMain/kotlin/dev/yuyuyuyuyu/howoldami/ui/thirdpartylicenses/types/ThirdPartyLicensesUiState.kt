package dev.yuyuyuyuyu.howoldami.ui.thirdpartylicenses.types

import dev.yuyuyuyuyu.howoldami.data.types.ThirdPartyLicense

data class ThirdPartyLicensesUiState(
    val thirdPartyLicenses: List<ThirdPartyLicense> = emptyList(),
)
