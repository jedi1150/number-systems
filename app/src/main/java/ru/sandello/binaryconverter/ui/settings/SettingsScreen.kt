package ru.sandello.binaryconverter.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsUiState = settingsUiState,
        onChangeThemeType = viewModel::updateThemeType,
//        onChangeLanguage = {},
    )
}

@Composable
fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onChangeThemeType: (ThemeType) -> Unit,
) {
    Column {
        Text(text = "Settings")
        Text(text = "Current theme: ${settingsUiState.themeType}")
        Button(onClick = { onChangeThemeType(ThemeType.SYSTEM) }) {
            Text(text = "System")
        }
        Button(onClick = { onChangeThemeType(ThemeType.LIGHT) }) {
            Text(text = "Light")
        }
        Button(onClick = { onChangeThemeType(ThemeType.DARK) }) {
            Text(text = "Dark")
        }
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    NumberSystemsTheme {
        SettingsScreen(
            settingsUiState = SettingsUiState(),
            onChangeThemeType = {},
        )
    }
}
