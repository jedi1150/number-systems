package ru.sandello.binaryconverter.ui.settings

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import java.util.Locale
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.PhoneLightDarkPreviews
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun SettingsScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        SettingsScreen(
            settingsUiState = SettingsUiState(
                themeType = ThemeType.SYSTEM,
                locale = Locale.US,
                isDigitGroupingEnabled = true,
            ),
            appVersion = "2.5.1",
            onChangeThemeType = {},
            onChangeLocale = {},
            onChangeDigitGrouping = {},
            onLinkClicked = {},
        )
    }
}
