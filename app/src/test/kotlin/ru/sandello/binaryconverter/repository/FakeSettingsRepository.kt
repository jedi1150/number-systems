package ru.sandello.binaryconverter.repository

import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType

internal class FakeSettingsRepository(
    initial: SettingsData = SettingsData(
        themeType = ThemeType.SYSTEM,
        locale = Locale.US,
        isDigitGroupingEnabled = true,
        isDigitGroupingInitialized = true,
        appLaunchCounter = 0,
    ),
) : SettingsRepository {
    private val _settingsData = MutableStateFlow(initial)
    override val settingsData = _settingsData.asStateFlow()

    override suspend fun setThemeType(themeType: ThemeType) {
        _settingsData.value = _settingsData.value.copy(themeType = themeType)
    }

    override suspend fun setLocale(locale: Locale) {
        _settingsData.value = _settingsData.value.copy(locale = locale)
    }

    override suspend fun setDigitGrouping(isDigitGroupingEnabled: Boolean) {
        _settingsData.value = _settingsData.value.copy(isDigitGroupingEnabled = isDigitGroupingEnabled)
    }

    override suspend fun setDigitGroupingInitialized(isDigitGroupingInitialized: Boolean) {
        _settingsData.value = _settingsData.value.copy(isDigitGroupingInitialized = isDigitGroupingInitialized)
    }

    override suspend fun incrementAppLaunchCounter() {
        _settingsData.value = _settingsData.value.copy(appLaunchCounter = _settingsData.value.appLaunchCounter + 1)
    }
}
