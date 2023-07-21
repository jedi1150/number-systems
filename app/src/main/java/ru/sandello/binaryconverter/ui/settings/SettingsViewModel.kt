package ru.sandello.binaryconverter.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.repository.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        settingsRepository.settingsData.map { userData: SettingsData ->
            SettingsUiState(themeType = userData.themeType)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(),
        )

    fun updateThemeType(themeType: ThemeType) {
        viewModelScope.launch {
            settingsRepository.setThemeType(themeType)
        }
    }
}
