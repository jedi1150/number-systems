package ru.sandello.binaryconverter.repository

import kotlinx.coroutines.flow.Flow
import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType
import javax.inject.Inject

class OfflineSettingsRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
) : SettingsRepository {
    override val settingsData: Flow<SettingsData> = settingsDataSource.settingsData

    override suspend fun setThemeType(themeType: ThemeType) = settingsDataSource.setThemeType(themeType)
}