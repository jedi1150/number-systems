package ru.sandello.binaryconverter.ui.settings

import ru.sandello.binaryconverter.model.data.ThemeType

data class SettingsUiState(
    var themeType: ThemeType = ThemeType.SYSTEM,
)
