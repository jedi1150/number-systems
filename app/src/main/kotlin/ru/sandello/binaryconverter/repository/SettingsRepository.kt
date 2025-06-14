package ru.sandello.binaryconverter.repository

import kotlinx.coroutines.flow.Flow
import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale

interface SettingsRepository {
    val settingsData: Flow<SettingsData>

    suspend fun setThemeType(themeType: ThemeType)

    suspend fun setLocale(locale: Locale)

    suspend fun setDigitGrouping(isDigitGroupingEnabled: Boolean)

    suspend fun setDigitGroupingInitialized(isDigitGroupingInitialized: Boolean)

    suspend fun incrementAppLaunchCounter()

}
