package ru.sandello.binaryconverter.model

import ru.sandello.binaryconverter.model.data.ThemeType
import java.util.Locale

data class SettingsData(
    val themeType: ThemeType = ThemeType.SYSTEM,
    val locale: Locale = Locale.ROOT,
    val appLaunchCounter: Int = 0,
)
