package dev.yuyuyuyuyu.howoldami.ui.howoldami.types

import dev.yuyuyuyuyu.howoldami.data.types.DateOfBirth

data class HowOldAmIUiState(
    val dateOfBirth: DateOfBirth = DateOfBirth(),
    val age: String = "",
)
