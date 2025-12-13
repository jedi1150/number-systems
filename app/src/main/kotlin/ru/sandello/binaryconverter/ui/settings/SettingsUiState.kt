package ru.sandello.binaryconverter.ui.settings

import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale

data class SettingsUiState(
    var themeType: ThemeType,
    var locale: Locale,
    val isDigitGroupingEnabled: Boolean,
) {
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
