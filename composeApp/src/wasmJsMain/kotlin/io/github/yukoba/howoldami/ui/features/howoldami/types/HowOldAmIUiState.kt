package io.github.yukoba.howoldami.ui.features.howoldami.types

import io.github.yukoba.howoldami.ui.components.types.DateOfBirth

data class HowOldAmIUiState(
    val dateOfBirth: DateOfBirth = DateOfBirth(),
    val age: String = "",
)