package ru.sandello.binaryconverter

import ru.sandello.binaryconverter.model.SettingsData

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val settings: SettingsData) : MainUiState
}
