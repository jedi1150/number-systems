package ru.sandello.binaryconverter.model

import java.util.Locale
import ru.sandello.binaryconverter.model.data.ThemeType

data class SettingsData(
    val themeType: ThemeType,
    val locale: Locale,
    val isDigitGroupingEnabled: Boolean,
    val isDigitGroupingInitialized: Boolean,
    val appLaunchCounter: Int,
)
