package ru.sandello.binaryconverter.repository

import kotlinx.coroutines.flow.Flow
import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale
import javax.inject.Inject

class OfflineSettingsRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
) : SettingsRepository {
    override val settingsData: Flow<SettingsData> = settingsDataSource.settingsData

    override suspend fun setThemeType(themeType: ThemeType) = settingsDataSource.setThemeType(themeType)

    override suspend fun setLocale(locale: Locale) = settingsDataSource.setLocale(locale)

    override suspend fun setDigitGrouping(isDigitGroupingEnabled: Boolean) = settingsDataSource.setDigitGrouping(isDigitGroupingEnabled)

    override suspend fun setDigitGroupingInitialized(isDigitGroupingEnabled: Boolean) = settingsDataSource.setDigitGroupingInitialized(isDigitGroupingEnabled)

    override suspend fun incrementAppLaunchCounter() = settingsDataSource.incrementAppLaunchCounter()

}
