package ru.sandello.binaryconverter.ui.settings

import java.util.Locale
import ru.sandello.binaryconverter.model.data.ThemeType

data class SettingsUiState(var themeType: ThemeType, var locale: Locale, val isDigitGroupingEnabled: Boolean) {
    val availableLocales = listOf(
        Locale.ROOT,
        Locale.forLanguageTag("en"),
        Locale.forLanguageTag("be"),
        Locale.forLanguageTag("ru"),
        Locale.forLanguageTag("kk"),
        Locale.forLanguageTag("uk"),
        Locale.forLanguageTag("uz"),
    ).sortedBy { locale -> locale.getDisplayLanguage(locale) }
}
