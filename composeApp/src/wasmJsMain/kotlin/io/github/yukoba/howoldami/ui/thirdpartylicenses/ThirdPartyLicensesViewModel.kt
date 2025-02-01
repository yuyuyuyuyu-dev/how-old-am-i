package io.github.yukoba.howoldami.ui.thirdpartylicenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yukoba.howoldami.domain.FetchThirdPartyLicensesUseCase
import io.github.yukoba.howoldami.ui.thirdpartylicenses.types.ThirdPartyLicensesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThirdPartyLicensesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ThirdPartyLicensesUiState())
    val uiState: StateFlow<ThirdPartyLicensesUiState> = _uiState.asStateFlow()

    private val fetchThirdPartyLicensesUseCase = FetchThirdPartyLicensesUseCase()

    init {
        viewModelScope.launch {
            fetchThirdPartyLicensesUseCase(
                onSuccess = { thirdPartyLicenses ->
                    _uiState.update { currentState ->
                        currentState.copy(thirdPartyLicenses = thirdPartyLicenses)
                    }
                }
            )
        }
    }
}
