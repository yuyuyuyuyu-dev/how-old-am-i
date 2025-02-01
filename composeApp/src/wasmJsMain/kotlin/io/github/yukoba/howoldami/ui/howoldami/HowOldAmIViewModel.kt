package io.github.yukoba.howoldami.ui.howoldami

import androidx.lifecycle.ViewModel
import io.github.yukoba.howoldami.data.types.DateOfBirth
import io.github.yukoba.howoldami.domain.calculateAgeUseCase
import io.github.yukoba.howoldami.ui.howoldami.types.HowOldAmIUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HowOldAmIViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HowOldAmIUiState())
    val uiState: StateFlow<HowOldAmIUiState> = _uiState.asStateFlow()

    fun onDateOfBirthChange(dateOfBirth: DateOfBirth) {
        _uiState.update { currentState ->
            currentState.copy(dateOfBirth = dateOfBirth)
        }

        calculateAgeUseCase(
            dateOfBirth = dateOfBirth,
            onSuccess = {
                _uiState.update { currentState ->
                    currentState.copy(age = it)
                }
            },
            onFailure = {
                _uiState.update { currentState ->
                    currentState.copy(age = "")
                }
                println("error: $it")
            }
        )
    }
}
