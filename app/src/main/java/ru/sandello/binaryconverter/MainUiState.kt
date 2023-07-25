package ru.sandello.binaryconverter

import ru.sandello.binaryconverter.model.SettingsData
import ru.sandello.binaryconverter.model.data.ThemeType

data class MainUiState(
    val settings: SettingsData = SettingsData(themeType = ThemeType.SYSTEM),
)
