package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import androidx.lifecycle.ViewModel
import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import dev.yuyuyuyuyu.howoldami.ui.model.DateOfBirth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@Inject
class HowOldAmIViewModelImpl(
    private val calculateAgeUseCase: CalculateAgeUseCase,
) : ViewModel(),
    HowOldAmIViewModel {
    private val _uiState =
        MutableStateFlow(
            HowOldAmIUiState(
                dateOfBirth = DateOfBirth("", "", ""),
                age = "",
            ),
        )
    override val uiState: StateFlow<HowOldAmIUiState> = _uiState.asStateFlow()

    override fun onDateOfBirthChanged(newValue: DateOfBirth) {
        _uiState.update { it.copy(dateOfBirth = newValue) }
        calculateAgeIfPossible(newValue)
    }

    private fun calculateAgeIfPossible(dateOfBirth: DateOfBirth) {
        val year = dateOfBirth.year.toIntOrNull()
        val month = dateOfBirth.month.toIntOrNull()
        val day = dateOfBirth.day.toIntOrNull()

        if (year != null && month != null && month in 1..12 && day != null && day in 1..31) {
            try {
                val age = calculateAgeUseCase(year, month, day)
                _uiState.update { it.copy(age = age.toString()) }
            } catch (e: Exception) {
                // Invalid date
                _uiState.update { it.copy(age = "") }
            }
        } else {
            _uiState.update { it.copy(age = "") }
        }
    }
}
