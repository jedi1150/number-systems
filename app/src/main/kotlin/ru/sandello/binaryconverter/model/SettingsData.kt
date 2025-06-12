package ru.sandello.binaryconverter.model

import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale

data class SettingsData(
    val themeType: ThemeType,
    val locale: Locale,
    val isDigitGroupingEnabled: Boolean,
    val isDigitGroupingInitialized: Boolean,
    val appLaunchCounter: Int,
)
