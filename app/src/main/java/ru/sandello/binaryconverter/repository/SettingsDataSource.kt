package ru.sandello.binaryconverter.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import ru.sandello.binaryconverter.Settings
import ru.sandello.binaryconverter.ThemeTypeProto
import ru.sandello.binaryconverter.copy
import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale
import javax.inject.Inject

class SettingsDataSource @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
) {
    val settingsData = settingsDataStore.data.map { settings ->
        SettingsData(
            themeType = when (settings.themeType) {
                ThemeTypeProto.SYSTEM -> ThemeType.SYSTEM
                ThemeTypeProto.LIGHT -> ThemeType.LIGHT
                ThemeTypeProto.DARK -> ThemeType.DARK
                ThemeTypeProto.UNRECOGNIZED -> ThemeType.SYSTEM
                null -> ThemeType.SYSTEM
            },
            locale = Locale.forLanguageTag(settings.languageTag),
        )
    }

    suspend fun setThemeType(themeType: ThemeType) {
        settingsDataStore.updateData { settings ->
            settings.copy {
                this.themeType = when (themeType) {
                    ThemeType.SYSTEM -> ThemeTypeProto.SYSTEM
                    ThemeType.LIGHT -> ThemeTypeProto.LIGHT
                    ThemeType.DARK -> ThemeTypeProto.DARK
                }
            }
        }
    }

    suspend fun setLocale(locale: Locale) {
        settingsDataStore.updateData { settings ->
            settings.copy {
                this.languageTag = locale.toLanguageTag()
            }
        }
    }
}
