package ru.sandello.binaryconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.sandello.binaryconverter.repository.OfflineSettingsRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    settingsRepository: OfflineSettingsRepository,
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = settingsRepository.settingsData.map { settingsData ->
        MainUiState.Success(settingsData)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )
}
