package io.github.yukoba.howoldami.ui.howoldami.types

import io.github.yukoba.howoldami.data.types.DateOfBirth

data class HowOldAmIUiState(
    val dateOfBirth: DateOfBirth = DateOfBirth(),
    val age: String = "",
)
