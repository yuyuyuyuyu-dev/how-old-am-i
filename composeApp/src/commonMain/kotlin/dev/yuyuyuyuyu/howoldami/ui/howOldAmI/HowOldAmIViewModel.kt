package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import dev.yuyuyuyuyu.howoldami.ui.model.DateOfBirth
import kotlinx.coroutines.flow.StateFlow

interface HowOldAmIViewModel {
    val uiState: StateFlow<HowOldAmIUiState>

    fun onDateOfBirthChanged(newValue: DateOfBirth)
}
