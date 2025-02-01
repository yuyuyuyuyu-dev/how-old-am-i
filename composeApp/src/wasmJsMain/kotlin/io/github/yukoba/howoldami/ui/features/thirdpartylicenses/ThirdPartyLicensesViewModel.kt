package io.github.yukoba.howoldami.ui.features.thirdpartylicenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yukoba.howoldami.ui.features.thirdpartylicenses.types.ThirdPartyLicensesUiState
import io.github.yukoba.howoldami.usecase.FetchThirdPartyLicensesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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