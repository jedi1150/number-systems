package ru.sandello.binaryconverter

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.repository.OfflineSettingsRepository
import ru.sandello.binaryconverter.repository.ReviewManager
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsRepository: OfflineSettingsRepository,
    private val reviewManager: ReviewManager,
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = settingsRepository.settingsData.map { settingsData ->
        MainUiState.Success(settingsData)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    init {
        viewModelScope.launch {
            settingsRepository.incrementAppLaunchCounter()
        }
    }

    fun requestReview(activity: Activity) {
        reviewManager.requestReview(activity)
    }

}
