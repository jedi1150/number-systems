package ru.sandello.binaryconverter.ui.settings

import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale

data class SettingsUiState(
    var themeType: ThemeType = ThemeType.SYSTEM,
    var locale: Locale = Locale.ROOT,
) {
    val availableLocales = mapOf(
        "" to R.string.locale_system,
        "en-US" to R.string.en,
        "be-BY" to R.string.be,
        "ru-RU" to R.string.ru,
        "kk-KZ" to R.string.kk,
        "uk-UA" to R.string.uk,
        "uz-UZ" to R.string.uz,
    ).toSortedMap()
}
